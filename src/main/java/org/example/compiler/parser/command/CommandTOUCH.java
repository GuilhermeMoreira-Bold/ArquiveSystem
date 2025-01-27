package org.example.compiler.parser.command;

public class CommandTOUCH extends CommandNode{
    String arquiveName;

    public CommandTOUCH(String arquiveName) {
        this.arquiveName = arquiveName;
    }

    @Override
    public <R> R accept(CommandVisitor<R> visitor) {
        return visitor.visitTouch(this);
    }

    @Override
    public String toString() {
        return "CommandTOUCH{" +
                "arquiveName='" + arquiveName + '\'' +
                '}';
    }
}
