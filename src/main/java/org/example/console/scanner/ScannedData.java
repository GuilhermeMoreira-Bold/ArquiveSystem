package org.example.console.scanner;

import org.example.console.lexer.Token;
import org.example.console.pipeline.component.IOComponent;

import java.util.List;

/**
 * Representa os dados resultantes da análise léxica (tokenização).
 * Esta classe encapsula a lista de tokens extraídos do código-fonte.
 */
public class ScannedData extends IOComponent<ScannedData> {

    /** Lista de tokens gerados pela análise léxica. */
    private List<Token> tokens;

    /**
     * Construtor da classe ScannedData.
     *
     * @param tokens Lista de tokens resultantes da análise léxica.
     */
    public ScannedData(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Retorna a lista de tokens extraídos do código-fonte.
     *
     * @return Lista de tokens.
     */
    public List<Token> getTokens() {
        return tokens;
    }
}
