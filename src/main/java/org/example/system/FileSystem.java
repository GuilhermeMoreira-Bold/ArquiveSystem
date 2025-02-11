package org.example.system;
import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.VirtualDisk;
import java.io.IOException;
import java.util.Map;

import static org.example.system.disk.DiskUtils.*;

public class FileSystem {
    Directory root;
    Directory current;
    VirtualDisk disk;
    private final boolean EXISTS = true;
    public Directory getCurrent() {
        return current;
    }

    public void setCurrent(Directory current) {
        this.current = current;
    }

    public FileSystem() throws IOException {
        disk = new VirtualDisk(EXISTS, DISK_NAME);
        initialize();
    }

    private void initialize() throws IOException {
      byte[] buffer =  disk.readDir(0);
      Entry rootEntry = Entry.toEntry(buffer);
      root = new Directory(rootEntry.getName(), null, rootEntry.getStatus(),rootEntry.getStartBlock());
      current = root;
      if (EXISTS) {
          addDataToDir(0,root);
          loadRootDirs();
      }
    }

    public VirtualDisk getDisk() {
        return disk;
    }


    private void loadRootDirs() throws IOException {
        for (Map.Entry<String, Directory> dir :
                root.getChildrens().entrySet()){
            addDataToDir(dir.getValue().getStaterBlock(),dir.getValue());
        }
    }
    private void addDataToDir(int index,Directory dir) throws IOException {
        byte[] buffer = disk.readDir(index);
        int offset = ENTRY_SIZE;

        for (int i = 0; i < buffer.length; i++) {
            if(offset == 4096) break;
            byte actualByte = buffer[offset];
            if(actualByte == FREE_AREA){
                offset++;
            }else{
               byte[] e = new byte[ENTRY_SIZE];
               System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
               Entry entry = Entry.toEntry(e);

               if(entry.getStatus() == 0){
                   offset += ENTRY_SIZE;
                   continue;
               }

               if(entry.getType() == 0){
                   dir.addSubdirectory(entry.getName(), new Directory(entry.getName(), dir, entry.getStatus(), entry.getStartBlock()));
               }else{
                   dir.addData(new Arquive(entry.getName(),"",entry.getSize(), entry.getStartBlock()));
               }
               offset += ENTRY_SIZE;

            }
        }
    }

    public void debugDataArea(int index) throws IOException {
        System.out.println(new String(disk.readDir(index)));
//
//        Entry en = Entry.toEntry(disk.readDir(index));
//        System.out.println(en);
    }

    @Override
    public String toString() {
        return "FileSystem{" +
                "root=" + root +
//                ", current=" + current +
                '}';
    }
}
