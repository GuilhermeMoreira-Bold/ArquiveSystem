
package org.example.compiler.parser.command;

import org.example.gui.ArquiveCatcher;
import org.example.system.FileSystem;
import org.example.system.arquives.Arquive;

import java.io.IOException;

public class CommandNANO extends CommandNode{

    private String arquiveName;
    public CommandNANO(String arquiveName){
        this.arquiveName = arquiveName;
    }

    @Override
    public String execute(FileSystem context) throws IOException {
        if(arquiveName==null){
            return "No arquive name specified";
        }

        for(Arquive a : context.getCurrent().getData()){
            if(a.getName().equals(arquiveName)){
                ArquiveCatcher arquive = new ArquiveCatcher(a);
                return "sucess";
            }
        }
        return "don't found";
    }
}
