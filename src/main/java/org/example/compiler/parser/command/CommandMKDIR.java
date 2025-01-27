package org.example.compiler.parser.command;

public class CommandMKDIR extends CommandNode{
    String dir;
    public CommandMKDIR(String directory) {
        this.dir = directory;
    }

    @Override
    public String toString() {
        return "CommandMKDIR{" +
                "dir='" + dir + '\'' +
                '}';
    }

    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitMKDIR(this);
    }
}
