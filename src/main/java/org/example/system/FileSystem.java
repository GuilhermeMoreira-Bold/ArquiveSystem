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
        disk = new VirtualDisk(EXISTS, "test.bin");
        initialize(EXISTS);
    }

    private void initialize(boolean exists) throws IOException {
      byte[] buffer =  disk.readDir(0);
      Entry rootEntry = Entry.toEntry(buffer);
      root = new Directory(rootEntry.getName(), null, rootEntry.getStatus(),0);
      current = root;
      if (exists) {
          addDataToRoot();
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

       if(disk.addSubDir(current.getStaterBlock(),new Entry(dir.getName(), (byte) 1, 1, (byte) 0,dir.getParent().getStaterBlock()))) {
           current.addSubdirectory(dir.getName(), dir);
        return;
       }
       throw new IOException("Directory already exists");
    }

    //subdir.getParent().getStaterBlock(), )


    private void addDataToRoot() throws IOException {
        byte[] buffer = disk.readDir(0);
        int offset = 269;


        for (int i = 0; i < buffer.length; i++) {
            if(offset == 4096) continue;
            byte actualByte = buffer[offset];
            if(actualByte == FREE_AREA){
                offset++;
            }else{
               byte[] e = new byte[269];
               System.arraycopy(buffer, offset, e, 0, 269);
               Entry entry = Entry.toEntry(e);

               if(entry.getType() == 0){
                   root.addSubdirectory(entry.getName(), new Directory(entry.getName(), root, entry.getStatus(),0));
               }else{
                   root.addData(new Arquive(entry.getName(),"",0));
               }
               offset += 269;

            }
        }
    }

    public void debugDataArea() throws IOException {
        System.out.println(new String(disk.readDir(0)));
    }

    @Override
    public String toString() {
        return "FileSystem{" +
                "root=" + root +
//                ", current=" + current +
                '}';
    }
}
