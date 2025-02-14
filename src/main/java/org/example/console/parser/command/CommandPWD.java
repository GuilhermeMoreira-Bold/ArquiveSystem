package org.example.console.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;

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

        return "pwd\n" + path + "\n";
    }

    @Override
    public String toString() {
        return "CommandPWD{}";
    }
}
