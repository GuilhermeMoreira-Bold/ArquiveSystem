package org.example.compiler.parser.command;

public interface CommandVisitor<T> {

    T visitCommandLine(CommandLineNode command);
    T visitMKDIR(CommandMKDIR command);
    T visitRMDIR(CommandRMDIR command);
    T visitLS(CommandLS command);
    T visitPWD(CommandPWD command);
    T visitTouch(CommandTOUCH command);
    T visitCD(CommandCD command);
}