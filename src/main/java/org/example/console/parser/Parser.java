package org.example.console.parser;

import org.example.console.lexer.Token;
import org.example.console.lexer.TokenType;
import org.example.console.parser.command.*;

import org.example.console.pipeline.pass.CompilationPass;
import org.example.console.scanner.ScannedData;
import org.example.gui.CommandCatcher;

import java.util.ArrayList;
import java.util.List;

public class Parser extends CompilationPass<ScannedData, ParsedData> {
    private int current = 0;
    List<Token> tokens;
    List<CommandNode> commands;

    @Override
    public Class<ScannedData> getInputType() {
        return ScannedData.class;
    }

    @Override
    public Class<ParsedData> getOutputType() {
        return ParsedData.class;
    }

    @Override
    public String getDebugName() {
        return "Parser";
    }

    @Override
    public ParsedData pass(ScannedData input) {
        current = 0;
        tokens = input.getTokens();
        commands = new ArrayList<>();

        if (tokens.isEmpty()) {
            throw new RuntimeException("No tokens found");
        }

        commands.add(command());

        while (!isAtEnd() && check(TokenType.SEMICOLON)) {
            consume(TokenType.SEMICOLON, "Unexpected command");
            commands.add(command());
        }
        return new ParsedData(commands);
    }

    private CommandNode command() {

        if (match(TokenType.CD)) {
            return commandCD();
        }
        if (match(TokenType.MKDIR)) {
            return commandMKDIR();
        }
        if (match(TokenType.TOUCH)) {
            return commandTouch();
        }
        if (match(TokenType.LS)) {
            return new CommandLS();
        }
        if (match(TokenType.PWD)) {
            return new CommandPWD();
        }
        if (match(TokenType.RM)) {
            return commandRMDIR();
        }
        if(match(TokenType.NANO)) {
            return commandNANO();
        }

        CommandCatcher.getInstance().getResults().add("\nUnexpected token: " + peek().lexeme);
        throw new RuntimeException("Unexpected token: " + peek().lexeme);
    }

    private CommandNode commandRMDIR() {
        check(TokenType.IDENTIFIER);
        String name = peek().lexeme;
        advance();
        return new CommandRM(name);
    }

    private CommandNode commandTouch() {
        check(TokenType.IDENTIFIER);
        String directory = peek().lexeme;
        advance();

        return new CommandTOUCH(directory);
    }

    private CommandCD commandCD() {
        if (!check(TokenType.IDENTIFIER, TokenType.TWODOTS)) {
            throw new RuntimeException("Not a valid directory name " + peek().lexeme + ", or missing one");
        }
        String directory = peek().lexeme;
        advance();
        return new CommandCD(directory);
    }

    private CommandMKDIR commandMKDIR() {
        check(TokenType.IDENTIFIER);
        String directory = peek().lexeme;
        advance();
        return new CommandMKDIR(directory);
    }

    private CommandNANO commandNANO(){
        check(TokenType.IDENTIFIER);
        String arquive = peek().lexeme;
        advance();
        return new CommandNANO(arquive);
    }

    private Token peek() {
        if (current >= tokens.size()) {
            throw new RuntimeException("Attempted to access an invalid index. Available tokens: " + tokens.size());
        }

        return tokens.get(current);
    }

    private boolean match(TokenType... types) {
        if (check(types)) {
            advance();
            return true;
        }
        return false;
    }

    private void advance() {
        if (current < tokens.size() - 1) {
            current++;
        }
    }

    private void consume(TokenType type, String message) {
        if (!match(type)) {
            throw new RuntimeException("Unexpected token type: " + message);
        }
    }

    private boolean check(TokenType... types) {
        for (TokenType type : types) {
            if (peek().type() == type) {
                return true;
            }
        }
        return false;
    }

    private boolean isAtEnd() {
        return current >= tokens.size() || peek().type() == TokenType.EOF;
    }
}
