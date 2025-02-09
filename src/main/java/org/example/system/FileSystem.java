package org.example.system;
import org.example.system.directories.Directory;
import org.example.system.disk.FileAllocationTable;
import org.example.system.disk.VirtualDisk;

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

    public FileSystem() {

    }

    public void initialize(){


    }

}
