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

    public Map<String, Directory> getChildrens() {
        return childrens;
    }

    public Directory(String name, Directory parent,byte status) {
        this.name = name;
        this.childrens = new HashMap<>();
        this.data = new ArrayList<>();
        this.status = status;
        this.parent = parent;
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
                    '}';
        }
        return "Directory{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", childrens=" + childrens +
                ", status=" + status +
                '}';
    }


}
