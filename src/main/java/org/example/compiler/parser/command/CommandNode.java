package org.example.compiler.parser.command;

import org.example.system.FileSystem;

import java.io.IOException;

public abstract class CommandNode {

    public abstract String execute(FileSystem context) throws IOException;

}
