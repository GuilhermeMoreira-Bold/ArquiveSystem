package org.example.system.arquives;

public class Arquive {
    String name;
    String data;
    int blocksSize;

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public int getSize() {
        return blocksSize;
    }

    public Arquive(String name, String data, int blocksSize) {
        this.name = name;
        this.data = data;
        this.blocksSize = blocksSize;
    }
}
