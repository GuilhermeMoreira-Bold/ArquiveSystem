package org.example.gui;

import org.example.compiler.interpreter.CommandExecutor;
import org.example.compiler.parser.Parser;
import org.example.compiler.pipeline.CompilationPipeline;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.compiler.scanner.Scanner;
import org.example.compiler.util.CMD;
import org.example.system.FileSystem;
import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static org.example.system.disk.DiskUtils.*;


public class FileSystemGUI extends JFrame {
    private final FileSystem fileSystem;
    private final CompilationPipeline pipeline;
    private JTree fileTree;
    private JTextArea outputArea;
    private JTextField commandInput;

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
        setLayout(new BorderLayout());

        fileTree = new JTree(fileSystem.getFileSystemTree());
        fileTree.setCellRenderer(new FileSystemTreeRenderer());
        fileTree.setShowsRootHandles(true);
        JScrollPane treeScroll = new JScrollPane(fileTree);

        JPanel terminalPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandInput = new JTextField();
        commandInput.addActionListener(new FileSystemGUI.ExecuteCommandListener());
        JButton executeButton = new JButton("Enter");
        executeButton.addActionListener(new ExecuteCommandListener());

        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);

        terminalPanel.add(outputScroll, BorderLayout.CENTER);
        terminalPanel.add(commandPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, terminalPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(220);

        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        commandPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        outputArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        commandInput.setFont(new Font("SansSerif", Font.PLAIN, 14));

        add(splitPane, BorderLayout.CENTER);

        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(() -> commandInput.requestFocusInWindow());
    }

    private Directory findDirectoryByPath(TreePath path) {
        Object[] nodes = path.getPath();
        Directory currentDir = fileSystem.getRoot();

        for (int i = 1; i < nodes.length; i++) {
            String dirName = nodes[i].toString();
            currentDir = currentDir.getChildrens().get(dirName);
            if (currentDir == null) return null;
        }

        return currentDir;
    }

    private void updateFileTree() {
        DefaultMutableTreeNode rootNode = fileSystem.getFileSystemTree();
        fileTree.setModel(new javax.swing.tree.DefaultTreeModel(rootNode));
    }

    private void arquivePopUp(Arquive a){
        // Cria um diálogo modal associado a este frame
        JDialog popupDialog = new JDialog(this, "Edição de Arquivo", true);
        popupDialog.setLayout(new BorderLayout());
        popupDialog.setSize(400, 300);
        popupDialog.setLocationRelativeTo(this); // Centraliza em relação à janela principal

        // Cria a área de texto para edição, preenchida com o nome do arquivo
        JTextArea textArea = new JTextArea(a.getData());
        textArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        popupDialog.add(scrollPane, BorderLayout.CENTER);

        // Cria o botão de salvar e define seu comportamento
        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama a função someFunction (que você deve implementar conforme necessário)
                saveArquive(a, textArea.getText());
                popupDialog.dispose(); // Fecha o popup após salvar
            }
        });
        popupDialog.add(saveButton, BorderLayout.SOUTH);

        // Exibe o popup
        popupDialog.setVisible(true);
    }
    //nano ei.txt

    private void saveArquive(Arquive base, String newContent) {
        try{

            fileSystem.getDisk().editEntry(new Entry(base.getName(),
                    base.getStaterBlock(), fileSystem.getCurrent().getStaterBlock(),
                    (int) Math.ceil((double) newContent.length() / CLUSTER_DATA_AREA_SIZE), BIT_ARQUIVE, BIT_FILLED), newContent);
        base.setData(newContent);
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private class ExecuteCommandListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = commandInput.getText();

            if (!command.isEmpty()) {
                try {
                    pipeline.execute(new CMD(command));
                    outputArea.setText("");

                    StringBuilder sb = new StringBuilder();

                    CommandCatcher catcher = CommandCatcher.getInstance();
                    ArquiveCatcher arquive = ArquiveCatcher.getInstance();

                    if(arquive.getArquive() != null) {
                        arquivePopUp(arquive.getArquive());
                        ArquiveCatcher a = new ArquiveCatcher(null);
                    }

                    catcher.getResults().forEach((String result) -> {
                        sb.append(result).append("\n");
                    });

                    outputArea.setText(sb.toString());
                    updateFileTree();
                } catch (UnexpectInputType | IOException | RuntimeException ex) {
                    outputArea.setText(outputArea.getText() + "\n$ Error: " + command + " " + ex.getMessage() + "\n");
                }
                commandInput.setText("");
            }

            commandInput.setText("");
        }
    }
}
