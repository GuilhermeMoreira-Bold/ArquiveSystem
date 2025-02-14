package org.example.console.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.Map;

public class CommandCD  extends CommandNode{
    String directoryName;

    public CommandCD(String directoryName) {
        this.directoryName = directoryName;
    }

    @Override
    public String execute(FileSystem context) {
        if(directoryName.equals("..")) {
            if(context.isInRoot()) {
                return "Already in root directory";
            } else {
                context.setCurrent(context.getCurrent().getParent());
                return toString();
            }

        }

        for(Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()){
            if(directoryName.equals(dirs.getKey())){
                context.setCurrent(dirs.getValue());
                return toString();
            }
        }
        return "cd " + directoryName + "\nno such directory\n";
    }

    @Override
    public String toString() {
        return String.format("cd %s\n", directoryName);
    }
}
