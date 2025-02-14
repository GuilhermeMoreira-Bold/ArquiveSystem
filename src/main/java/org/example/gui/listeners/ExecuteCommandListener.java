package org.example.gui.listeners;

import org.example.console.pipeline.CompilationPipeline;
import org.example.console.pipeline.execptions.UnexpectInputType;
import org.example.console.util.CMD;
import org.example.gui.ArquiveCatcher;
import org.example.gui.CommandCatcher;
import org.example.gui.FileSystemGUI;
import org.example.gui.panels.TerminalPanel;
import org.example.system.FileSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ExecuteCommandListener implements ActionListener {

    private final FileSystemGUI parent;
    private final TerminalPanel terminalPanel;
    private final CompilationPipeline pipeline;

    public ExecuteCommandListener(FileSystemGUI parent,
                                  TerminalPanel terminalPanel,
                                  CompilationPipeline pipeline,
                                  FileSystem fileSystem) {
        this.parent = parent;
        this.terminalPanel = terminalPanel;
        this.pipeline = pipeline;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = terminalPanel.getCommandInput().getText();

        if (!command.isEmpty()) {
            try {
                CommandCatcher.getInstance().getResults().clear();
                pipeline.execute(new CMD(command));

                CommandCatcher catcher = CommandCatcher.getInstance();
                ArquiveCatcher arquive = ArquiveCatcher.getInstance();

                catcher.getResults().forEach(result ->
                        terminalPanel.getOutputArea().append(result + "\n")
                );

                if (arquive.getArquive() != null) {
                    parent.showArquiveDialog(arquive.getArquive());
                    new ArquiveCatcher(null);
                }

                terminalPanel.getOutputArea().setCaretPosition(
                        terminalPanel.getOutputArea().getDocument().getLength()
                );

                parent.updateFileTree();
                parent.updateDiskUsagePanel();
            } catch (UnexpectInputType | IOException | RuntimeException ex) {
                terminalPanel.getOutputArea().append("$ Error: " + command + " " + ex.getMessage() + "\n");
                terminalPanel.getOutputArea().setCaretPosition(
                        terminalPanel.getOutputArea().getDocument().getLength()
                );
            }

            terminalPanel.getCommandInput().setText("");
        }

        terminalPanel.getCommandInput().setText("");
    }
}
