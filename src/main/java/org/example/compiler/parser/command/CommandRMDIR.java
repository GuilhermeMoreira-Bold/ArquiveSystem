package org.example.compiler.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;

import java.io.IOException;
import java.util.Map;

public class CommandRMDIR extends CommandNode{
    String name;
    public CommandRMDIR(String name) {

    this.name = name;
    }

    @Override
    public String execute(FileSystem context) {
        for(Map.Entry<String, Directory> dir : context.getCurrent().getChildrens().entrySet()) {
            if(dir.getKey().equals(name)) {
                Directory d = dir.getValue();
                Entry entry = new Entry(d.getName(),d.getStaterBlock(),context.getCurrent().getStaterBlock(), 0,(byte)0,d.getStatus());

                try{
                    context.getDisk().removeDir(entry);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }

                context.getCurrent().removeSubdirectory(dir.getValue());
                return "RM sucess";
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
