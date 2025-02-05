package org.example.compiler.parser.command;

import org.example.system.arquives.FileSystem;

public abstract class CommandNode {

    public abstract String execute(FileSystem context);

}
