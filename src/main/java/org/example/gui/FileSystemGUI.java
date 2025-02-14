package org.example.gui;

import org.example.console.interpreter.CommandExecutor;
import org.example.console.parser.Parser;
import org.example.console.pipeline.CompilationPipeline;
import org.example.console.pipeline.execptions.UnexpectInputType;
import org.example.console.scanner.Scanner;
import org.example.console.util.CMD;
import org.example.gui.listeners.ExecuteCommandListener;
import org.example.gui.panels.DiskUsagePanel;
import org.example.gui.dialogs.ArquiveDialog;
import org.example.gui.dialogs.DiskUsageDialog;
import org.example.gui.menu.FileSystemMenuBar;
import org.example.gui.panels.FileTreePanel;
import org.example.gui.panels.TerminalPanel;
import org.example.system.FileSystem;
import org.example.system.arquives.Arquive;
import org.example.system.disk.Entry;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static org.example.system.disk.DiskUtils.*;


public class FileSystemGUI extends JFrame {
    private final FileSystem fileSystem;
    private final CompilationPipeline pipeline;
    private FileTreePanel fileTreePanel;
    private TerminalPanel terminalPanel;
    private DiskUsagePanel diskPanelComponent;

    public FileSystemGUI() throws IOException {
        fileSystem = new FileSystem();
        pipeline = new CompilationPipeline();
        pipeline.insertStage(new Scanner())
                .insertStage(new Parser())
                .insertStage(new CommandExecutor(fileSystem))
                .insertStage(new CommandCatcherStage());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }

        initUI();
    }

    private void initUI() {

        setTitle("Arquive System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        setJMenuBar(new FileSystemMenuBar(this));

        fileTreePanel = new FileTreePanel(fileSystem);

        diskPanelComponent = new DiskUsagePanel(fileSystem.getDisk());

        terminalPanel = new TerminalPanel();

        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                fileTreePanel, diskPanelComponent);
        leftSplit.setDividerLocation(350);
        leftSplit.setOneTouchExpandable(true);
        leftSplit.setDividerSize(12);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftSplit, terminalPanel);
        mainSplit.setDividerLocation(240);
        mainSplit.setOneTouchExpandable(true);
        mainSplit.setDividerSize(18);

        add(mainSplit, BorderLayout.CENTER);

        ExecuteCommandListener listener = new ExecuteCommandListener(this, terminalPanel, pipeline, fileSystem);
        terminalPanel.getCommandInput().addActionListener(listener);
        terminalPanel.getExecuteButton().addActionListener(listener);

        updateFileTree();

        updateDiskUsagePanel();

        setVisible(true);
        SwingUtilities.invokeLater(() -> terminalPanel.getCommandInput().requestFocusInWindow());
    }

    public void updateFileTree() {
        DefaultMutableTreeNode rootNode = fileSystem.getFileSystemTree();
        fileTreePanel.getFileTree().setModel(new javax.swing.tree.DefaultTreeModel(rootNode));
    }

    public void showArquiveDialog(Arquive a) {
        ArquiveDialog dialog = new ArquiveDialog(this, a, newContent -> saveArquive(a, newContent));
        dialog.setVisible(true);
    }

    private void saveArquive(Arquive base, String newContent) {
        try {
            fileSystem.getDisk().editEntry(
                            new Entry(base.getName(),
                            base.getStaterBlock(),
                            fileSystem.getCurrent().getStaterBlock(),
                            (int) Math.ceil((double) newContent.length() / CLUSTER_DATA_AREA_SIZE),
                            BIT_ARQUIVE,
                            BIT_FILLED),
                            newContent);
            base.setData(newContent);
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public void showDiskUsageDialog() {
        DiskUsageDialog dialog = new DiskUsageDialog(this, fileSystem.getDisk());
        dialog.setVisible(true);
    }

    public void updateDiskUsagePanel() {
        diskPanelComponent.updateDiskInfo();
    }
}
