package org.example.compiler.interpreter;

import org.example.compiler.parser.ParsedData;
import org.example.compiler.parser.command.CommandNode;
import org.example.compiler.pipeline.pass.CompilationPass;
import org.example.system.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor extends CompilationPass<ParsedData,CommandResult> {
    List<CommandNode> commands;
    List<String> results = new ArrayList<>();
    final FileSystem fileSystem;
    public CommandExecutor(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }
    @Override
    public Class<ParsedData> getInputType() {
        return ParsedData.class;
    }

    @Override
    public Class<CommandResult> getOutputType() {
        return CommandResult.class;
    }

    @Override
    public String getDebugName() {
        return "CommandExecutor";
    }

    @Override
    public CommandResult pass(ParsedData input) throws IOException {
       commands = input.commands;

       for (CommandNode command : commands) {
           String currentPath = fileSystem.isInRoot() ? "/" : fileSystem.getCurrent().getPath();

            results.add(currentPath + ": " + command.execute(fileSystem));
       }
       for (String result : results) {
           System.out.println(result);
       }
        return new CommandResult(results);
    }
}
