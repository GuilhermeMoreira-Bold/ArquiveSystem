package org.example.system.directories;

import java.util.List;
import org.example.system.arquives.Arquive;

public class Directory {
    String name;
    List<Arquive> data;
    List<Directory> childrens;
    Directory parent;

    public Directory(List<Arquive> data,String name, List<Directory> childrens, Directory parent) {
        this.name = name;
        this.childrens = childrens;
        this.parent = parent;
        this.data = data;
    }

    public void addData(Arquive arquive) {
        data.add(arquive);
    }
    public void removeData(Arquive arquive) {
        data.remove(arquive);
    }

    public void addSubdirectory(Directory subdirectory) {
        childrens.add(subdirectory);
    }
    public void removeSubdirectory(Directory subdirectory) {
        childrens.remove(subdirectory);
    }

    public String getName() {
        return name;
    }

    public List<Arquive> getData() {
        return data;
    }

    public List<Directory> getChildrens() {
        return childrens;
    }

    public Directory getParent() {
        return parent;
    }
}
