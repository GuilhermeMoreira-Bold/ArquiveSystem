package org.example.system;
import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.VirtualDisk;

import java.io.IOException;

import static org.example.system.disk.DiskUtils.*;

public class FileSystem {
    Directory root;
    Directory current;
    VirtualDisk disk;
    public Directory getCurrent() {
        return current;
    }

    public void setCurrent(Directory current) {
        this.current = current;
    }

    public FileSystem() throws IOException {
        disk = new VirtualDisk(true, "test.bin");
        initialize(true);
    }

    private void initialize(boolean exists) throws IOException {
      byte[] buffer =  disk.readDir(0);
      Entry rootEntry = Entry.toEntry(buffer);
      root = new Directory(rootEntry.getName(), null, rootEntry.getStatus(),0);
      if (exists) {
          addDataToDir();
      }
    }

    public void createDirectory(){

    }

    //subdir.getParent().getStaterBlock(), new Entry(subdir.getName(), (byte) 1, 1, (byte) 0,subdir.getParent().getStaterBlock())


    private void addDataToDir() throws IOException {
        byte[] buffer = disk.readDir(0);
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
                root.addSubdirectory(entry.getName(), new Directory(entry.getName(), root, entry.getStatus(),entry.getStartBlock()));
            }else{
                root.addData(new Arquive(entry.getName(), "", entry.getSize()));
            }

            offset += ENTRY_SIZE;
        }
    }

    @Override
    public String toString() {
        return "FileSystem{" +
                "root=" + root +
                ", current=" + current +
                '}';
    }
}
