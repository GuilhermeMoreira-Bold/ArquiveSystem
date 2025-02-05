package org.example.system.arquives;
import org.example.system.directories.Directory;

public class FileSystem {
    Directory root;
    Directory current;
    public Directory getCurrent() {
        return current;
    }

    public void setCurrent(Directory current) {
        this.current = current;
    }

    public FileSystem(Directory root) {
        this.root = root;
        current = root;
    }

}
