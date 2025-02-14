package org.example.console.util;

import org.example.console.pipeline.component.IOComponent;

public class CMDInput extends IOComponent<CMDInput> {
    private final String command;

    /**
     * Construtor que recebe um comando como uma string.
     *
     * @param command O comando fornecido como uma string.
     */
    public CMDInput(String command) {
        this.command = command;
    }

    /**
     * Retorna o comando armazenado.
     *
     * @return O comando fornecido.
     */
    public String getCommand() {
        return command;
    }
}
