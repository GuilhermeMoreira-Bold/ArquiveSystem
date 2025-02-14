package org.example.compiler.lexer;

/**
 * Representa um token identificado pelo analisador léxico.
 * Um token contém um lexema, um tipo, uma linha de origem e, opcionalmente, um valor literal.
 */
public class Token {

    /** O lexema correspondente ao token. */
    public String lexeme;

    /** A linha do código-fonte onde o token foi encontrado. */
    int line;

    /** O valor literal associado ao token, se aplicável. */
    Object literal;

    /** O tipo do token, definido pela enumeração TokenType. */
    TokenType type;

    /**
     * Construtor para inicializar um novo token.
     *
     * @param type    O tipo do token.
     * @param lexeme  O lexema correspondente.
     * @param literal O valor literal associado, se houver.
     * @param line    A linha onde o token foi encontrado.
     */
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
        this.literal = literal;
    }

    /**
     * Retorna uma representação em string do token, contendo seu lexema e tipo.
     *
     * @return Uma string formatada representando o token.
     */
    @Override
    public String toString() {
        return "Token{" +
                "lexeme='" + lexeme + '\'' +
                "type=" + type.toString() +
                '}';
    }

    /**
     * Retorna o tipo do token como um enum genérico.
     *
     * @param <E> O tipo do enum.
     * @return O tipo do token.
     */
    public <E extends Enum<E>> Enum<E> type() {
        return (Enum<E>) type;
    }

    /**
     * Retorna o valor literal associado ao token, se houver.
     *
     * @return O valor literal ou null caso não exista.
     */
    public Object literal() {
        return literal;
    }
}
