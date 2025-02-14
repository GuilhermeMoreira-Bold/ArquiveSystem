package org.example.console.util;

import org.example.console.pipeline.component.IOComponent;

public class CMDInput extends IOComponent<CMDInput> {
    private final String command;

    public CMDInput(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
