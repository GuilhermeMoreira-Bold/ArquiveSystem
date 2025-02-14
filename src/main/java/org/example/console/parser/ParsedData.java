package org.example.console.parser;

import org.example.console.parser.command.CommandNode;
import org.example.console.pipeline.component.IOComponent;

import java.util.List;

public class ParsedData extends IOComponent <ParsedData>{
    public final List<CommandNode> commands;
    public ParsedData(List<CommandNode> commands){
        this.commands = commands;
    }
}
