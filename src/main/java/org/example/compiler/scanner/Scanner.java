package org.example.compiler.scanner;

import org.example.compiler.lexer.Token;
import org.example.compiler.lexer.TokenType;

import org.example.compiler.pipeline.pass.CompilationPass;
import org.example.compiler.util.CMD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner extends CompilationPass<CMD,ScannedData> {

    private List<Token> tokens;
    private static Map<String, TokenType> keyWords = new HashMap<String, TokenType>() {};
    public Scanner(){
        keyWords.put("mkdir", TokenType.MKDIR);
        keyWords.put("rmdir", TokenType.RMDIR);
        keyWords.put("touch", TokenType.TOUCH);
        keyWords.put("rm", TokenType.RM);
        keyWords.put("ls", TokenType.LS);
        keyWords.put("pwd", TokenType.PWD);
        keyWords.put("cd", TokenType.CD);
    };
    private String source;
    private int line;
    private int start;
    private int current;
    @Override
    public Class<CMD> getInputType() {
        return CMD.class;
    }

    @Override
    public Class<ScannedData> getOutputType() {
        return ScannedData.class;
    }

    @Override
    public String getDebugName() {
        return "Scanner";
    }

    @Override
    public ScannedData pass(CMD input) {
        resetInternalState(input);
        scanTokens();
        return new ScannedData(tokens);
    }
    private void scanTokens() {
        while(!isAtEnd()){
            syncCursos();
            scanTOken();
        }
        makeToken(TokenType.EOF, null, null);
    }
    private void syncCursos() {
        start = current;
    }
    private void scanTOken() {
        char c = advance();
        switch (c){
            case ' ':
            case '\t':
            case '\r':
                break;
                case '\n':
                    line++;
                    break;
            case '/':
            case '.':
                if(advance() == '.'){
                    //TODO
                }
                break;
            case ';':
                makeToken(TokenType.SEMICOLON);
                break;
            default:
                if(isDigit(c)){
                    number();
                    break;
                }
                if(isAlpha(c)){
                    identifier();
                    break;
                }
                else{
                    throw new RuntimeException("Unexpected character: " + c);
                }

        }
    }

    private void identifier() {
        while(isAlphaNumeric(peek()) || peek() == '.' || peek() == '_') advance();
        String value = source.substring(start, current);
        TokenType type;
        type = TokenType.IDENTIFIER;

        if(keyWords.containsKey(value)){
            type = keyWords.get(value);
        }

        makeToken(type);
    }
    private void number() {
        while(isDigit(peek())){
            advance();
        }

        String lexeme = source.substring(start, current);
        Float number = Float.parseFloat(lexeme);
        makeToken(TokenType.NUMBER, number);
    }
    private char peek(){
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private void makeToken(TokenType type){
        String lexeme = source.substring(start, current);
        makeToken(type, lexeme, null);
    }
    private void makeToken(TokenType type, Object literal) {
        String lexeme = source.substring(start, current);
        Token token = new Token(type, lexeme, literal, line);
        tokens.add(token);
    }

    private void makeToken(TokenType type, String lexeme, Object literal) {
        Token token = new Token(type, lexeme, literal, line);
        tokens.add(token);
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }
    private boolean isAlpha(char c){
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }
    private void resetInternalState(CMD file) {
        line = 1;
        start = 0;
        current = 0;
        source = file.getSource();
        tokens = new ArrayList<>();
    }
}
