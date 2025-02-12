package org.example.system.directories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.system.arquives.Arquive;

public class Directory {
    String name;
    List<Arquive> data;
    Map<String,Directory> childrens;
    Directory parent;
    byte status;
    int staterBlock;

    public void setStaterBlock(int staterBlock) {
        this.staterBlock = staterBlock;
    }

    public Map<String, Directory> getChildrens() {
        return childrens;
    }

    public Directory(String name, Directory parent,byte status, int staterBlock) {
        this.name = name;
        this.childrens = new HashMap<>();
        this.data = new ArrayList<>();
        this.status = status;
        this.parent = parent;
        this.staterBlock = staterBlock;
    }

    public Directory(String name,Directory parent, byte status) {
        this.name = name;
        this.parent = parent;
        this.status = status;
        this.childrens = new HashMap<>();
        this.data = new ArrayList<>();
    }

    public int getStaterBlock() {
        return staterBlock;
    }

    public byte getStatus() {
        return status;
    }

    public void addData(Arquive arquive) {
        data.add(arquive);
    }
    public void removeData(Arquive arquive) {
        data.remove(arquive);
    }

    public void addSubdirectory(String name,Directory subdirectory) {
        childrens.put(name,subdirectory);
    }
    public void removeSubdirectory(Directory subdirectory) {
        childrens.remove(subdirectory.name);
    }

    public String getName() {
        return name;
    }

    public List<Arquive> getData() {
        return data;
    }

    public Directory getParent() {
        return parent;
    }

    @Override
    public String toString() {
        if (parent != null) {
            return "Directory{" +
                    "name='" + name + '\'' +
                    ", data=" + data +
                    ", childrens=" + childrens +
                    ", parent=" + parent.getName() +
                    ", status=" + status +
                    ", starter_block=" + staterBlock +
                    '}';
        }
        return "Directory{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", childrens=" + childrens +
                ", status=" + status +
                ", starter_block=" + staterBlock +
                '}';
    }

    public String getPath() {
        StringBuilder path = new StringBuilder();
        Directory d = this;

        while(true){
            if(d == null) break;
            path.insert(0, d.getName().equals("/") ? "" : "/" + d.getName());
            d = d.getParent();
        }

        return path.toString();
    }
}
