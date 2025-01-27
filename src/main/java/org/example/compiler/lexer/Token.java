package org.example.compiler.lexer;

public class Token {

    public String lexeme;
    int line;
    Object literal;
    TokenType type;

    public Token(TokenType type, String lexeme, Object literal,int line) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
        this.literal = literal;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexeme='" + lexeme + '\'' +
               "type=" + type.toString() +
                '}';
    }

    public <E extends Enum<E>> Enum<E> type() {
        return (Enum<E>) type;
    }

    public Object literal() {
        return literal;
    }
}