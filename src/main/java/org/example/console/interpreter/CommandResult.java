package org.example.console.interpreter;

import org.example.console.pipeline.component.IOComponent;

import java.util.List;

public class CommandResult extends IOComponent<CommandResult> {
  List<String> results;

    public CommandResult(List<String> results) {
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }
}