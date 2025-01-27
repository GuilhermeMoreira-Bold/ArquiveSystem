package org.example.compiler.parser;

import org.example.compiler.parser.command.CommandNode;
import org.example.compiler.pipeline.component.IOComponent;

import java.util.List;

public class ParsedData extends IOComponent <ParsedData>{
    public final List<CommandNode> commands;
    public ParsedData(List<CommandNode> commands){
        this.commands = commands;
    }
}
