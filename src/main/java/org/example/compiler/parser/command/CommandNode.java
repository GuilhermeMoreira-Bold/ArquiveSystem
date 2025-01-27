package org.example.compiler.parser.command;

public abstract class CommandNode {
    public abstract <R> R accept(CommandVisitor<R> visitor);

}
