package org.example.compiler.scanner;

import org.example.compiler.lexer.Token;
import org.example.compiler.lexer.TokenType;
import org.example.compiler.pipeline.pass.CompilationPass;
import org.example.compiler.util.CMD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável pela análise léxica, convertendo o código de entrada em uma lista de tokens.
 * O scanner percorre o código fonte e identifica palavras-chave, símbolos e valores numéricos.
 */
public class Scanner extends CompilationPass<CMD, ScannedData> {

    /** Lista de tokens extraídos do código-fonte. */
    private List<Token> tokens;

    /** Mapa de palavras-chave para seus respectivos tipos de token. */
    private static Map<String, TokenType> keyWords = new HashMap<String, TokenType>() {};

    /**
     * Construtor da classe Scanner. Inicializa o mapa de palavras-chave.
     */
    public Scanner() {
        keyWords.put("mkdir", TokenType.MKDIR);
        keyWords.put("rm", TokenType.RM);
        keyWords.put("touch", TokenType.TOUCH);
        keyWords.put("ls", TokenType.LS);
        keyWords.put("pwd", TokenType.PWD);
        keyWords.put("cd", TokenType.CD);
        keyWords.put("nano", TokenType.NANO);
    };

    /** Código-fonte a ser analisado. */
    private String source;

    /** Número da linha atual durante a análise. */
    private int line;

    /** Índice de início do lexema atual. */
    private int start;

    /** Índice atual do caractere sendo processado. */
    private int current;

    /**
     * Retorna o tipo de entrada esperado pelo Scanner.
     *
     * @return A classe CMD, que representa o comando de entrada.
     */
    @Override
    public Class<CMD> getInputType() {
        return CMD.class;
    }

    /**
     * Retorna o tipo de saída produzido pelo Scanner.
     *
     * @return A classe ScannedData, que contém os tokens extraídos.
     */
    @Override
    public Class<ScannedData> getOutputType() {
        return ScannedData.class;
    }

    /**
     * Retorna o nome da fase de compilação para depuração.
     *
     * @return String "Scanner".
     */
    @Override
    public String getDebugName() {
        return "Scanner";
    }

    /**
     * Executa a análise léxica no comando recebido.
     *
     * @param input O comando a ser analisado.
     * @return Um objeto ScannedData contendo os tokens extraídos.
     */
    @Override
    public ScannedData pass(CMD input) {
        resetInternalState(input);
        scanTokens();
        return new ScannedData(tokens);
    }

    /**
     * Percorre o código-fonte e gera a lista de tokens.
     */
    private void scanTokens() {
        while (!isAtEnd()) {
            syncCursos();
            scanTOken();
        }
        makeToken(TokenType.EOF, null, null);

        System.out.println(tokens);
    }

    /**
     * Atualiza os índices para o início de um novo token.
     */
    private void syncCursos() {
        start = current;
    }

    /**
     * Identifica e classifica o próximo token no código-fonte.
     */
    private void scanTOken() {
        char c = advance();
        switch (c) {
            case ' ':
            case '\t':
            case '\r':
                break;
            case '\n':
                line++;
                break;
            case '/':
            case '.':
                if (advance() == '.') {
                    makeToken(TokenType.TWODOTS);
                }
                break;
            case ';':
                makeToken(TokenType.SEMICOLON);
                break;
            default:
                if (isDigit(c)) {
                    number();
                    break;
                }
                if (isAlpha(c)) {
                    identifier();
                    break;
                } else {
                    throw new RuntimeException("Unexpected character: " + c);
                }
        }
    }

    /**
     * Identifica identificadores e palavras-chave no código-fonte.
     */
    private void identifier() {
        while (isAlphaNumeric(peek()) || peek() == '.' || peek() == '_') advance();
        String value = source.substring(start, current);
        TokenType type = TokenType.IDENTIFIER;

        if (keyWords.containsKey(value)) {
            type = keyWords.get(value);
        }

        makeToken(type);
    }

    /**
     * Identifica e processa números no código-fonte.
     */
    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        String lexeme = source.substring(start, current);
        Float number = Float.parseFloat(lexeme);
        makeToken(TokenType.NUMBER, number);
    }

    /**
     * Retorna o próximo caractere sem avançar no índice.
     *
     * @return O caractere atual ou '\0' se for o final do código.
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Cria um novo token com base no lexema identificado.
     *
     * @param type O tipo do token.
     */
    private void makeToken(TokenType type) {
        String lexeme = source.substring(start, current);
        makeToken(type, lexeme, null);
    }

    /**
     * Cria um token com um valor literal associado.
     *
     * @param type    O tipo do token.
     * @param literal O valor literal associado ao token.
     */
    private void makeToken(TokenType type, Object literal) {
        String lexeme = source.substring(start, current);
        Token token = new Token(type, lexeme, literal, line);
        tokens.add(token);
    }

    /**
     * Cria e adiciona um token à lista de tokens extraídos.
     *
     * @param type    O tipo do token.
     * @param lexeme  O lexema correspondente.
     * @param literal O valor literal, se houver.
     */
    private void makeToken(TokenType type, String lexeme, Object literal) {
        Token token = new Token(type, lexeme, literal, line);
        tokens.add(token);
    }

    /**
     * Verifica se um caractere é alfanumérico.
     *
     * @param c O caractere a ser verificado.
     * @return true se for uma letra ou um número, false caso contrário.
     */
    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    /**
     * Verifica se um caractere é uma letra do alfabeto.
     *
     * @param c O caractere a ser verificado.
     * @return true se for uma letra, false caso contrário.
     */
    private boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    /**
     * Verifica se um caractere é um dígito numérico.
     *
     * @param c O caractere a ser verificado.
     * @return true se for um número, false caso contrário.
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Avança para o próximo caractere no código-fonte e o retorna.
     *
     * @return O caractere atual antes de avançar.
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Verifica se chegou ao final do código-fonte.
     *
     * @return true se todos os caracteres foram processados, false caso contrário.
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Reinicializa o estado interno do scanner para processar um novo código-fonte.
     *
     * @param file O comando contendo o código-fonte a ser analisado.
     */
    private void resetInternalState(CMD file) {
        line = 1;
        start = 0;
        current = 0;
        source = file.getSource();

        if (tokens == null) {
            tokens = new ArrayList<>();
        } else {
            tokens.clear();
        }
    }
}