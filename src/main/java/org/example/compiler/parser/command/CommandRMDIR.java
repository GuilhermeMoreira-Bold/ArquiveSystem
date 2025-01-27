package org.example.compiler.parser.command;

public class CommandRMDIR extends CommandNode{
    public CommandRMDIR() {
    }

    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitRMDIR(this);
    }

    @Override
    public String toString() {
        return "CommandRMDIR{}";
    }
}
