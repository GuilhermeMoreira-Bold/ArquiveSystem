package org.example.console.scanner;

import org.example.console.lexer.Token;
import org.example.console.pipeline.component.IOComponent;

import java.util.List;

public class ScannedData extends IOComponent<ScannedData> {
    private List<Token> tokens;

    public ScannedData(List<Token> tokens) {
        this.tokens = tokens;
    }
    public List<Token> getTokens() {
        return tokens;
    }
}
