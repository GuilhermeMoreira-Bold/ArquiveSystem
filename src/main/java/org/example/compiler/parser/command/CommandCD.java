package org.example.compiler.parser.command;

public class CommandCD  extends CommandNode{
    String directoryName;

    public CommandCD(String directoryName) {
        this.directoryName = directoryName;
    }

    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitCD(this);
    }

    @Override
    public String toString() {
        return "CommandCD{" +
                "directoryName='" + directoryName + '\'' +
                '}';
    }
}
