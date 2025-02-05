package org.example.compiler.parser.command;

import org.example.system.arquives.FileSystem;
import org.example.system.directories.Directory;

import java.util.Map;

public class CommandCD  extends CommandNode{
    String directoryName;

    public CommandCD(String directoryName) {
        this.directoryName = directoryName;
    }

    @Override
    public String execute(FileSystem context) {
        boolean found = false;
        for(Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()){
            if(directoryName.equals(dirs.getKey()) && !found){
                context.setCurrent(dirs.getValue());
                found = true;
            }
        }
        if(!found)  return "No such directory: " + directoryName;
        return "sucess";
    }

    @Override
    public String toString() {
        return "CommandCD{" +
                "directoryName='" + directoryName + '\'' +
                '}';
    }
}
