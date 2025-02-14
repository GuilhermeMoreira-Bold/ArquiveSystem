package org.example.console.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.disk.Entry;

import java.io.IOException;
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
            if(arquive.getName().equals(arquiveName)){
                return "touch " + arquiveName + "\n" + arquiveName + " already exists";
            }
        }

        try {
            int starterBlock =  context.getDisk().addEntry(new Entry(arquiveName, (byte) 1, 1, (byte) 1, context.getCurrent().getStaterBlock()));
            context.getCurrent().addData(new Arquive(arquiveName,"",1, starterBlock));

        }catch (IOException e){
            throw new RuntimeException(e);}

        return "touch " + arquiveName + "\nsuccess\n";
    }

    @Override
    public String toString() {
        return "CommandTOUCH{" +
                "arquiveName='" + arquiveName + '\'' +
                '}';
    }
}