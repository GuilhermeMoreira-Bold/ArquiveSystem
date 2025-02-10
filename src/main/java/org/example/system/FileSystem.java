package org.example.system;
import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.VirtualDisk;
import java.io.IOException;
import java.util.Arrays;
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
        disk = new VirtualDisk(EXISTS, "test.bin");
        initialize(EXISTS);
    }

    private void initialize(boolean exists) throws IOException {
      byte[] buffer =  disk.readDir(0);
      Entry rootEntry = Entry.toEntry(buffer);
      root = new Directory(rootEntry.getName(), null, rootEntry.getStatus(),rootEntry.getStartBlock());
      current = root;
      if (exists) {
          addDataToDir(0,root);
          loadRootDirs();
      }
    }

    public void createDirectory(Directory dir) throws IOException {
        if(getCurrent().getChildrens() != null) {
            for (Map.Entry<String, Directory> dirs :
                    getCurrent().getChildrens().entrySet()) {
                if (dir.getName().equals(dirs.getKey())) {
                    System.out.println(" already exists");
                    return;
                }
            }
        }

       int starterBlock = disk.addSubDir(current.getStaterBlock(),new Entry(dir.getName(), (byte) 1, 1, (byte) 0,current.getStaterBlock()));
       dir.setStaterBlock(starterBlock);
       current.addSubdirectory(dir.getName(), dir);
    }
    public void removeDirectory(String name) throws IOException {
        for(Map.Entry<String, Directory> dir : getCurrent().getChildrens().entrySet()) {
            if(dir.getKey().equals(name)) {
                Directory d = dir.getValue();
                Entry entry = new Entry(d.getName(),d.getStaterBlock(),getCurrent().getStaterBlock(), 0,(byte)0,d.getStatus());
                disk.removeDir(entry);
                getCurrent().removeSubdirectory(dir.getValue());
            }
        }
    }

    public void moveDir(String name){
        boolean found = false;
        for(Map.Entry<String, Directory> dirs : getCurrent().getChildrens().entrySet()){
            if(name.equals(dirs.getKey())){
                setCurrent(dirs.getValue());
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println(" does not exist");
        }
    }

    //subdir.getParent().getStaterBlock(), )

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
               System.arraycopy(buffer, offset, e, 0, 269);
               Entry entry = Entry.toEntry(e);

               if(entry.getStatus() == 0){
                   offset += ENTRY_SIZE;
               }

               if(entry.getType() == 0){
                   dir.addSubdirectory(entry.getName(), new Directory(entry.getName(), dir, entry.getStatus(), entry.getStartBlock()));
               }else{
                   dir.addData(new Arquive(entry.getName(),"",0));
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
