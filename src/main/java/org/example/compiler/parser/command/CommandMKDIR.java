package org.example.compiler.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.Map;

public class CommandMKDIR extends CommandNode{
    String dir;
    public CommandMKDIR(String directory) {
        this.dir = directory;
    }

    @Override
    public String toString() {
        return "CommandMKDIR{" +
                "dir='" + dir + '\'' +
                '}';
    }

    @Override
    public String execute(FileSystem context) {
        if(context.getCurrent().getChildrens() != null) {
            for (Map.Entry<String, Directory> dirs :
                    context.getCurrent().getChildrens().entrySet()) {
                if (dir.equals(dirs.getKey())) {
                    return dir + " already exists";
                }
            }
        }
//        context.getCurrent().addSubdirectory(dir,new Directory(dir, context.getCurrent()));
        return "success";
    }
}
