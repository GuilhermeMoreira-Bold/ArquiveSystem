package org.example.console.parser.command;

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
                    return "mkdir " + dirName + "\nerror\n";
                }
            }
        }

        int starterBlock = context.getDisk().addEntry(new Entry(dir.getName(), (byte) 0, 1, (byte) 1, context.getCurrent().getStaterBlock()));
        dir.setStaterBlock(starterBlock);
        context.getCurrent().addSubdirectory(dirName, dir);

        return "mkdir " + dirName + "\nsuccess\n";
    }
}
