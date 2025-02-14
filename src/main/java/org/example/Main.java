package org.example;

import org.example.compiler.interpreter.CommandExecutor;
import org.example.compiler.parser.Parser;
import org.example.compiler.pipeline.CompilationPipeline;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.compiler.scanner.Scanner;
import org.example.compiler.util.CMD;
import org.example.gui.CommandCatcherStage;
import org.example.gui.FileSystemGUI;
import org.example.system.FileSystem;

import javax.swing.*;
import java.io.IOException;

/**
 * A classe <b>Main</b> é responsável por iniciar a aplicação.
 * Ela oferece dois modos de execução:
 * <ul>
 *     <li><b>Modo GUI (Interface Gráfica)</b>: Ativado caso o primeiro argumento recebido seja <code>--gui</code>.</li>
 *     <li><b>Modo Console</b>: Caso contrário, executa o pipeline de compilação/leitura e processa comandos via terminal.</li>
 * </ul>
 */
public class Main {
    /**
     * Método principal (entry point) da aplicação.
     *
     * @param args Argumentos de linha de comando. O uso de <code>--gui</code> inicializa a interface gráfica.
     * @throws UnexpectInputType Exceção lançada caso ocorra algum erro no pipeline (por exemplo, tipo de input inesperado).
     */
    public static void main(String[] args) throws UnexpectInputType {
        if (args.length > 0 && args[0].equals("--gui")) {
            SwingUtilities.invokeLater(() -> {
                try {
                    new FileSystemGUI().setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            try {
                CompilationPipeline pipeline = new CompilationPipeline();
                CMD cmd = new CMD("src/main/resources/cmd.test", true);
                FileSystem fl = new FileSystem();

                pipeline.insertStage(new Scanner())
                        .insertStage(new Parser())
                        .insertStage(new CommandExecutor(fl))
                        .insertStage(new CommandCatcherStage());

                pipeline.execute(cmd);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
