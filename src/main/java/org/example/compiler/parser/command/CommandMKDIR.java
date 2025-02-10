package org.example.compiler.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;

import java.io.IOException;
import java.util.Map;

public class CommandMKDIR extends CommandNode{
    String dirName;
    public CommandMKDIR(String directory) {
        this.dirName = directory;
    }

    @Override
    public String toString() {
        return "CommandMKDIR{" +
                "dir='" + dirName + '\'' +
                '}';
    }

    @Override
    public String execute(FileSystem context) throws IOException {
        Directory dir = new Directory(dirName,context.getCurrent(),(byte) 1);

        if(context.getCurrent().getChildrens() != null) {
            for (Map.Entry<String, Directory> dirs :
                    context.getCurrent().getChildrens().entrySet()) {
                if (dir.getName().equals(dirs.getKey())) {
                    System.out.println(" already exists");
                    return "error";
                }
            }
        }

        int starterBlock = context.getDisk().addSubDir(context.getCurrent().getStaterBlock(),new Entry(dir.getName(), (byte) 1, 1, (byte) 0,context.getCurrent().getStaterBlock()));
        dir.setStaterBlock(starterBlock);
        context.getCurrent().addSubdirectory(dir.getName(), dir);

        return "success";
    }
}
