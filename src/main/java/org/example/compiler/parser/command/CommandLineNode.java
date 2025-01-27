package org.example.compiler.parser.command;

public class CommandLineNode extends CommandNode{
    public CommandLineNode() {

    }

    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitCommandLine(this);
    }
}
