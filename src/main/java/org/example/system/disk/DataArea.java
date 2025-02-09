package org.example.system.disk;

import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.handlers.DataAreaHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.example.system.disk.DiskUtils.*;

public class DataArea {
    private final DataAreaHandler IOHandler;

    public DataArea(RandomAccessFile raf,boolean isNew) throws IOException {
        this.IOHandler = new DataAreaHandler(raf);
        IOHandler.initialize(isNew);
        initialize(isNew);
    }

    public void initialize(boolean isNew) throws IOException {
        IOHandler.initialize(isNew);
        if (isNew) {
            createRootDirTable();
        }
    }


    public void createRootDirTable() throws FileNotFoundException {
        try(RandomAccessFile raf = new RandomAccessFile(DISK_NAME, "r")){
            Entry root = new Entry("/", (byte)1,1, (byte) 1, 0);
            IOHandler.writeAt (root.toBytes(),0);
        }catch (Exception e){
            throw new RuntimeException(e);
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


    public byte[] readAllDisk() throws IOException {
        return IOHandler.read();
    }

    public byte[] readEntry(int cluster) throws IOException {
        return IOHandler.readAt(cluster);
    }
    public byte[] readEntryAt(int clusterId) throws IOException {
        return IOHandler.readAt(clusterId);
    }
}