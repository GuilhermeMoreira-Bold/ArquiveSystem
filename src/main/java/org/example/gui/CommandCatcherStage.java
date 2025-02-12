package org.example.gui;

import org.example.compiler.interpreter.CommandResult;
import org.example.compiler.pipeline.pass.CompilationPass;

import java.io.IOException;

public class CommandCatcherStage extends CompilationPass<CommandResult, CommandCatcher> {
    @Override
    public Class<CommandResult> getInputType() {
        return CommandResult.class;
    }

    @Override
    public Class<CommandCatcher> getOutputType() {
        return CommandCatcher.class;
    }

    @Override
    public String getDebugName() {
        return "Command Catcher Stage";
    }

    @Override
    public CommandCatcher pass(CommandResult input) throws IOException {
        return new CommandCatcher(input);
    }
}
