package org.example.compiler.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.List;
import java.util.Map;

public class CommandLS extends CommandNode{
    @Override
    public String execute(FileSystem context) {
        StringBuilder content = new StringBuilder();
        for(Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()){
            if(dirs.getValue().getChildrens() != null) {
                content.append(dirs.getKey() + "\n");
            }
        }
        List<Arquive> arquives = context.getCurrent().getData();
        for (Arquive arquive : arquives) {
            content.append(arquive.getName() + "\n");
        }

//        try {
//             context.debugDataArea(1);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "";
        return content.toString();
    }

    @Override
    public String toString() {
        return "CommandLS{}";
    }
}
