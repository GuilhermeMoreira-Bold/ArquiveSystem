package org.example.compiler.scanner;

import org.example.compiler.lexer.Token;
import org.example.compiler.pipeline.component.IOComponent;

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
