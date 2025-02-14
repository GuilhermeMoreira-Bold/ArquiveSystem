package org.example.compiler.interpreter;

import org.example.compiler.parser.ParsedData;
import org.example.compiler.parser.command.CommandNode;
import org.example.compiler.pipeline.pass.CompilationPass;
import org.example.system.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor extends CompilationPass<ParsedData, CommandResult> {
    // Lista de comandos que foram analisados pelo parser.
    private List<CommandNode> commands;
    // Lista de resultados que será preenchida durante a execução dos comandos.
    private List<String> results = new ArrayList<>();
    // O sistema de arquivos que é manipulado durante a execução dos comandos.
    private final FileSystem fileSystem;

    /**
     * Construtor que recebe o sistema de arquivos que será usado para executar os comandos.
     *
     * @param fileSystem O sistema de arquivos a ser usado.
     */
    public CommandExecutor(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Class<ParsedData> getInputType() {
        return ParsedData.class; // O tipo de entrada esperado é ParsedData
    }

    @Override
    public Class<CommandResult> getOutputType() {
        return CommandResult.class; // O tipo de saída esperado é CommandResult
    }

    @Override
    public String getDebugName() {
        return "CommandExecutor"; // Nome usado para depuração
    }

    @Override
    public CommandResult pass(ParsedData input) throws IOException {
        commands = input.commands; // Pega a lista de comandos do ParsedData

        // Para cada comando, executa e armazena o resultado.
        for (CommandNode command : commands) {
            // Determina o caminho atual do sistema de arquivos.
            String currentPath = fileSystem.isInRoot() ? "/" : fileSystem.getCurrent().getPath();

            // Executa o comando e armazena o resultado.
            results.add(currentPath + ": " + command.execute(fileSystem));
        }

        // Imprime todos os resultados no console para depuração.
        for (String result : results) {
            System.out.println(result);
        }

        // Retorna os resultados encapsulados em um objeto CommandResult
        return new CommandResult(results);
    }
}
