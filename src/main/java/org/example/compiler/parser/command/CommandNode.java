package org.example.compiler.parser.command;

import org.example.system.FileSystem;

public abstract class CommandNode {

    public abstract String execute(FileSystem context);

}
