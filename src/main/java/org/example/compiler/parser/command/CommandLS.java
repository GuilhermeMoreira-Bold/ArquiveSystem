package org.example.compiler.parser.command;

public class CommandLS extends CommandNode{
    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitLS(this);
    }

    @Override
    public String toString() {
        return "CommandLS{}";
    }
}
