package org.example.compiler.parser.command;

public class CommandPWD  extends  CommandNode{
    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitPWD(this);
    }

    @Override
    public String toString() {
        return "CommandPWD{}";
    }
}
