package org.example.gui;

import org.example.compiler.interpreter.CommandExecutor;
import org.example.compiler.parser.Parser;
import org.example.compiler.pipeline.CompilationPipeline;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.compiler.scanner.Scanner;
import org.example.compiler.util.CMD;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


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

        initUI();
    }

    private void initUI() {

        setTitle("File System Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        fileTree = new JTree(fileSystem.getFileSystemTree());
        fileTree.setCellRenderer(new FileSystemTreeRenderer());
        JScrollPane treeScroll = new JScrollPane(fileTree);
        treeScroll.setPreferredSize(new Dimension(200, 600));
        add(treeScroll, BorderLayout.WEST);

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem newFileMenuItem = new JMenuItem("New File");
        JMenuItem newFolderMenuItem = new JMenuItem("New Folder");

        newFileMenuItem.addActionListener(e -> {
            TreePath selectedPath = fileTree.getSelectionPath();
            if (selectedPath == null) return;

            FileSystem.FileSystemTreeNode selectedNode = (FileSystem.FileSystemTreeNode) selectedPath.getLastPathComponent();
            if (!selectedNode.isDirectory()) return;

            String fileName = JOptionPane.showInputDialog("Nome do novo arquivo:");
            if (fileName == null || fileName.trim().isEmpty()) return;

            Directory selectedDirectory = findDirectoryByPath(selectedPath);
            if (selectedDirectory != null) {
                fileSystem.createFile(selectedDirectory, fileName);
                updateFileTree();
            }
        });

        newFolderMenuItem.addActionListener(e -> {
            TreePath selectedPath = fileTree.getSelectionPath();
            if (selectedPath == null) return;

            FileSystem.FileSystemTreeNode selectedNode = (FileSystem.FileSystemTreeNode) selectedPath.getLastPathComponent();
            if (!selectedNode.isDirectory()) return;

            String dirName = JOptionPane.showInputDialog("Nome da nova pasta:");
            if (dirName == null || dirName.trim().isEmpty()) return;

            Directory selectedDirectory = findDirectoryByPath(selectedPath);
            if (selectedDirectory != null) {
                fileSystem.createDirectory(selectedDirectory, dirName);
                updateFileTree();
            }
        });

        popupMenu.add(newFileMenuItem);
        popupMenu.add(newFolderMenuItem);

        JPanel mainPanel = new JPanel(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandInput = new JTextField();
        commandInput.addActionListener(new FileSystemGUI.ExecuteCommandListener());
        JButton executeButton = new JButton("Enter");

        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);

        mainPanel.add(outputScroll, BorderLayout.CENTER);
        mainPanel.add(commandPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        executeButton.addActionListener(new ExecuteCommandListener());
        fileTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = fileTree.getRowForLocation(e.getX(), e.getY());
                    if (row != -1) {
                        fileTree.setSelectionRow(row);
                        popupMenu.show(fileTree, e.getX(), e.getY());
                    }
                }
            }
        });
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
