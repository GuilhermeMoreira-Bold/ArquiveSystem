package org.example.compiler.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.ArrayList;
import java.util.List;

public class CommandPWD  extends  CommandNode{
    @Override
    public String execute(FileSystem context) {
        StringBuilder path = new StringBuilder();
        Directory d = context.getCurrent();
        while(true){
            if(d == null) break;
            path.insert(0, d.getName().equals("/") ? "" : "/" + d.getName());
            d = d.getParent();
        }

        return path.toString();
    }

    @Override
    public String toString() {
        return "CommandPWD{}";
    }
}
