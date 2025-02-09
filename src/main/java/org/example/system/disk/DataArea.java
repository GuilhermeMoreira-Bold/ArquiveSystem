package org.example.system.disk;

import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.handlers.DataAreaHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import static org.example.system.disk.DiskUtils.*;

public class DataArea {
    private final DataAreaHandler IOHandler;

    public DataArea(RandomAccessFile raf,boolean isNew) throws IOException {
        this.IOHandler = new DataAreaHandler(raf);
        IOHandler.initialize(isNew);
    }

    public void intialize(boolean isNew) throws IOException {
        IOHandler.initialize(isNew);
    }


    public void createRootDirTable() throws FileNotFoundException {
        try(RandomAccessFile raf = new RandomAccessFile(DISK_NAME, "r")){
            Entry root = new Entry("/", (byte)1,1, (byte) 1, 0);
            IOHandler.writeAt (root.toBytes(),0);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public Directory getRootDir() throws IOException {
        byte[] buffer = IOHandler.readAt(0);
        Entry rootEntry = Entry.toEntry(buffer);

        Directory root = new Directory(rootEntry.getName(), null, rootEntry.getStatus());
        addDataToDir(root, 0);
        return root;
    }

    private void addDataToDir(Directory dir, int dirCluster) throws IOException {
        byte[] buffer = IOHandler.readAt(dirCluster);
        int offset = ENTRY_SIZE;


        while (offset + ENTRY_SIZE <= buffer.length) {
            byte[] entryBytes = new byte[ENTRY_SIZE];

            System.arraycopy(buffer, offset, entryBytes, 0, ENTRY_SIZE);

            if (entryBytes[0] == FREE_AREA) {
                offset += ENTRY_SIZE; // Pula a área livre
                continue;
            }

            Entry entry = Entry.toEntry(entryBytes);

            if (entry.getType() == 0) { // Verifica se é dir
                dir.addSubdirectory(entry.getName(), new Directory(entry.getName(), dir, entry.getStatus()));
            }else{
                dir.addData(new Arquive(entry.getName(), "", entry.getSize()));
            }

            offset += ENTRY_SIZE;
        }
    }

    public void writeEntry(int cluster, Entry entry) throws IOException {
            byte[] clusterContent = IOHandler.readAt(cluster);
            boolean hasSpace = true;

            for (int i = 0; i <= clusterContent.length; i++) {
                byte c = clusterContent[i];
                if (c ==  FREE_AREA) {
                    for(int j = 0; j < 269; j++){
                        if (clusterContent[i + j] != FREE_AREA) {
                            hasSpace = false;
                            break;
                        }
                    }
                    if(hasSpace){
                        IOHandler.writeAt(entry.toBytes(), (int) (DATA_AREA_OFFSET + ((long) cluster * CLUSTER) + i));
                        return;
                    }

                }
            }
        if (!hasSpace) {
            throw new IOException("No space available in cluster " + cluster);
        }
    }
}
