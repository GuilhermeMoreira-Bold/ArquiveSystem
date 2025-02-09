package org.example.compiler.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.Map;

public class CommandRMDIR extends CommandNode{
    String name;
    public CommandRMDIR(String name) {

    this.name = name;
    }

    @Override
    public String execute(FileSystem context) {
        for(Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()) {
            if(dirs.getKey().equals(name)) {
                context.getCurrent().removeSubdirectory(dirs.getValue());
                return "RM " + name + " sucess";
            }
        }
        for (Arquive aqruive : context.getCurrent().getData()) {
            if(aqruive.getName().equals(name)) {
                context.getCurrent().removeData(aqruive);
                return "RM sucess";
            }
        }
        return "RM error";

    }

    @Override
    public String toString() {
        return "CommandRMDIR{}";
    }
}
