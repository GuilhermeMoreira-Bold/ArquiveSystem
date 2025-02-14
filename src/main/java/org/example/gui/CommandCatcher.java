package org.example.gui;

import org.example.console.interpreter.CommandResult;
import org.example.console.pipeline.component.IOComponent;

import java.util.ArrayList;
import java.util.List;

public class CommandCatcher extends IOComponent<CommandCatcher> {

    private static CommandCatcher instance;

    private final List<String> results;

    public CommandCatcher(CommandResult commandResult) {
        this.results = commandResult.getResults();
        instance = this;
    }

    private CommandCatcher(List<String> results) {
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }

    public static CommandCatcher getInstance() {
        if(instance == null) {
            instance = new CommandCatcher(new ArrayList<>());
        }
        return instance;
    }
}
