package org.example.compiler.parser;

import org.example.compiler.lexer.Token;
import org.example.compiler.lexer.TokenType;
import org.example.compiler.parser.command.*;

import org.example.compiler.pipeline.pass.CompilationPass;
import org.example.compiler.scanner.ScannedData;

import java.util.ArrayList;
import java.util.List;

public class Parser extends CompilationPass<ScannedData, ParsedData> {
    private int current = 0;
    List<Token> tokens;
    List<CommandNode> commands;

    @Override
    public Class<ScannedData> getInputType() {
        return null;
    }

    @Override
    public Class<ParsedData> getOutputType() {
        return null;
    }

    @Override
    public String getDebugName() {
        return "Parser";
    }

    @Override
    public ParsedData pass(ScannedData input) {
        tokens = input.getTokens();
        commands = new ArrayList<>();
        commands.add(command());
        while(!isAtEnd() && check(TokenType.SEMICOLON) ) {
            consume(TokenType.SEMICOLON, "Unexpected command");
            commands.add(command());

        }
        for (CommandNode command : commands
             ) {
            System.out.println(command.toString());

        }
        return new ParsedData(commands);
    }

    private CommandNode command(){

       if(match(TokenType.CD)){
           if(!check(TokenType.IDENTIFIER))
           {
               throw new RuntimeException("Not a valid directory name " + peek().lexeme + ", or missing one");
           }
            String directory = peek().lexeme;
           advance();

            return new CommandCD(directory);
       }
       if(match(TokenType.MKDIR)){
           check(TokenType.IDENTIFIER);
           String directory = peek().lexeme;
            advance();
           return new CommandMKDIR(directory);
       }
       if(match(TokenType.TOUCH)){
           check(TokenType.IDENTIFIER);
           String directory = peek().lexeme;
           advance();

           return new CommandTOUCH(directory);
       }
       if(match(TokenType.LS)){
        return new CommandLS();
       }
       if(match(TokenType.PWD)){
         return new CommandPWD();
       }
       if(match(TokenType.RMDIR)){
           return new CommandRMDIR();
       }
        throw new RuntimeException("Unexpected token: " + peek().lexeme);
    }
    private Token peek(){

        return tokens.get(current);
    }

    private boolean match(TokenType... types){
        if(check(types)){
            advance();
            return true;
        }
        return false;
    }
    private void advance(){
        current++;
    }
    private void consume(TokenType type, String message){
        if(!match(type)){
            throw new RuntimeException("Unexpected token type: " + message);
        }
    }
    private boolean check(TokenType... types){
        for(TokenType type : types){
            if(peek().type() == type){
                return true;
            }
        }
        return false;
    }
    private boolean isAtEnd(){
        return peek().type() == TokenType.EOF;
    }
}
