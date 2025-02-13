package org.example.system.arquives;

public class Arquive {
    String name;
    String data;
    int blocksSize;
    int staterBlock;

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSize() {
        return blocksSize;
    }

    public int getStaterBlock() {
        return staterBlock;
    }

    public void setStaterBlock(int staterBlock) {
        this.staterBlock = staterBlock;
    }

    public Arquive(String name, String data, int blocksSize, int staterBlock) {
        this.staterBlock = staterBlock;
        this.name = name;
        this.data = data;
        this.blocksSize = blocksSize;
    }


}
