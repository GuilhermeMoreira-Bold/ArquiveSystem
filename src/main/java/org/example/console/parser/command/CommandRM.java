package org.example.console.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;

import java.io.IOException;
import java.util.Map;

public class CommandRM extends CommandNode{
    String name;
    public CommandRM(String name) {

    this.name = name;
    }

    @Override
    public String execute(FileSystem context) {
        for(Map.Entry<String, Directory> dir : context.getCurrent().getChildrens().entrySet()) {
            if(dir.getKey().equals(name)) {
                if(dir.getValue().getSize() > 0){
                    return "ERROR DIR NOT EMPTY";
                }
                Directory d = dir.getValue();
                Entry entry = new Entry(d.getName(),d.getStaterBlock(),context.getCurrent().getStaterBlock(), 0,(byte)0,d.getStatus());

                try{
                    context.getDisk().removeEntry(entry);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }

                context.getCurrent().removeSubdirectory(dir.getValue());
                return "rm" + dir.getKey() + "\nsuccess\n";
            }
        }
        for (Arquive arquive : context.getCurrent().getData()) {
            if(arquive.getName().equals(name)) {
                context.getCurrent().removeData(arquive);
                try{
                    context.getDisk().removeEntry(new Entry(arquive.getName(), arquive.getStaterBlock(), context.getCurrent().getStaterBlock(), arquive.getSize(), (byte)1,(byte)1));
                }catch (IOException e){
                    throw new RuntimeException(e);
                }

                return "rm" + arquive.getName() + "\nsuccess\n";
            }
        }
        return "RM error";

    }

    @Override
    public String toString() {
        return "CommandRMDIR{}";
    }
}
