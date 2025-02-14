package org.example.console.parser;

import org.example.console.lexer.Token;
import org.example.console.lexer.TokenType;
import org.example.console.parser.command.*;

import org.example.console.pipeline.pass.CompilationPass;
import org.example.console.scanner.ScannedData;
import org.example.gui.CommandCatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * O Parser analisa a lista de tokens e os converte em uma estrutura de árvore de comandos.
 * Ele implementa um analisador sintático para reconhecer comandos e seus argumentos.
 */
public class Parser extends CompilationPass<ScannedData, ParsedData> {

    /** Índice do token atual sendo analisado. */
    private int current = 0;

    /** Lista de tokens gerados pelo Scanner. */
    private List<Token> tokens;

    /** Lista de comandos reconhecidos a partir da sequência de tokens. */
    private List<CommandNode> commands;

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

    /**
     * Executa a análise sintática sobre os tokens extraídos pelo Scanner.
     *
     * @param input Dados tokenizados a serem analisados.
     * @return Estrutura de comandos reconhecida a partir dos tokens.
     * @throws RuntimeException Se nenhum token for encontrado ou se ocorrer um erro de sintaxe.
     */
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

    /**
     * Analisa um comando baseado nos tokens atuais.
     *
     * @return Nó de comando correspondente ao token identificado.
     * @throws RuntimeException Se um token inesperado for encontrado.
     */
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
        if (match(TokenType.NANO)) {
            return commandNANO();
        }

        CommandCatcher.getInstance().getResults().add("\nUnexpected token: " + peek().lexeme);
        throw new RuntimeException("Unexpected token: " + peek().lexeme);
    }

    /** Analisa um comando `rm`. */
    private CommandNode commandRMDIR() {
        check(TokenType.IDENTIFIER);
        String name = peek().lexeme;
        advance();
        return new CommandRM(name);
    }

    /** Analisa um comando `touch`. */
    private CommandNode commandTouch() {
        check(TokenType.IDENTIFIER);
        String directory = peek().lexeme;
        advance();
        return new CommandTOUCH(directory);
    }

    /** Analisa um comando `cd`. */
    private CommandCD commandCD() {
        if (!check(TokenType.IDENTIFIER, TokenType.TWODOTS)) {
            throw new RuntimeException("Not a valid directory name " + peek().lexeme + ", or missing one");
        }
        String directory = peek().lexeme;
        advance();
        return new CommandCD(directory);
    }

    /** Analisa um comando `mkdir`. */
    private CommandMKDIR commandMKDIR() {
        check(TokenType.IDENTIFIER);
        String directory = peek().lexeme;
        advance();
        return new CommandMKDIR(directory);
    }

    /** Analisa um comando `nano`. */
    private CommandNANO commandNANO() {
        check(TokenType.IDENTIFIER);
        String arquive = peek().lexeme;
        advance();
        return new CommandNANO(arquive);
    }

    /**
     * Retorna o token atual sem avançar.
     *
     * @return Token atual.
     * @throws RuntimeException Se o índice do token for inválido.
     */
    private Token peek() {
        if (current >= tokens.size()) {
            throw new RuntimeException("Attempted to access an invalid index. Available tokens: " + tokens.size());
        }
        return tokens.get(current);
    }

    /**
     * Verifica se o próximo token corresponde a um dos tipos fornecidos e avança se for o caso.
     *
     * @param types Tipos de token a serem verificados.
     * @return `true` se um dos tokens corresponder, `false` caso contrário.
     */
    private boolean match(TokenType... types) {
        if (check(types)) {
            advance();
            return true;
        }
        return false;
    }

    /** Avança para o próximo token, se possível. */
    private void advance() {
        if (current < tokens.size() - 1) {
            current++;
        }
    }

    /**
     * Consome um token esperado e avança, lançando erro se o token não corresponder.
     *
     * @param type Tipo de token esperado.
     * @param message Mensagem de erro caso o token não corresponda.
     * @throws RuntimeException Se o token encontrado for inesperado.
     */
    private void consume(TokenType type, String message) {
        if (!match(type)) {
            throw new RuntimeException("Unexpected token type: " + message);
        }
    }

    /**
     * Verifica se o próximo token corresponde a um dos tipos fornecidos.
     *
     * @param types Tipos de token a serem verificados.
     * @return `true` se um dos tokens corresponder, `false` caso contrário.
     */
    private boolean check(TokenType... types) {
        for (TokenType type : types) {
            if (peek().type() == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se todos os tokens foram processados.
     *
     * @return `true` se a análise chegou ao fim, `false` caso contrário.
     */
    private boolean isAtEnd() {
        return current >= tokens.size() || peek().type() == TokenType.EOF;
    }
}
