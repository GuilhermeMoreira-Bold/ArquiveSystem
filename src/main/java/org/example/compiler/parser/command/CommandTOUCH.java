package org.example.compiler.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.arquives.FileSystem;

import java.util.List;

public class CommandTOUCH extends CommandNode{
    String arquiveName;

    public CommandTOUCH(String arquiveName) {
        this.arquiveName = arquiveName;
    }

    @Override
    public String execute(FileSystem context) {
        List<Arquive> arquives = context.getCurrent().getData();
        for (Arquive arquive : arquives) {
            if(arquive.getName() == arquiveName){
                return arquiveName + " already exists";
            }
        }
        context.getCurrent().addData(new Arquive(arquiveName,"",arquiveName.length()));
        return "success";

    }

    @Override
    public String toString() {
        return "CommandTOUCH{" +
                "arquiveName='" + arquiveName + '\'' +
                '}';
    }
}
