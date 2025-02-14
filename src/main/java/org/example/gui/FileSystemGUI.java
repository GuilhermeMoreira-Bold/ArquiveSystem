package org.example.gui;

import org.example.console.interpreter.CommandExecutor;
import org.example.console.parser.Parser;
import org.example.console.pipeline.CompilationPipeline;
import org.example.console.pipeline.execptions.UnexpectInputType;
import org.example.console.scanner.Scanner;
import org.example.console.util.CMD;
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
    private CircularProgressBar diskUsageBar;
    private JLabel diskInfoLabel;

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

        createMenuBar();

        fileTree = new JTree(fileSystem.getFileSystemTree());
        fileTree.setCellRenderer(new FileSystemTreeRenderer());
        JLabel treeLabel = new JLabel("File System Tree", SwingConstants.CENTER);
        treeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.add(treeLabel, BorderLayout.NORTH);
        treePanel.add(new JScrollPane(fileTree), BorderLayout.CENTER);

        diskUsageBar = new CircularProgressBar();
        JLabel diskLabel = new JLabel("Disk Usage", SwingConstants.CENTER);
        diskLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        diskInfoLabel = new JLabel("Used / Total", SwingConstants.CENTER);

        JPanel diskPanel = new JPanel(new BorderLayout());
        diskPanel.add(diskLabel, BorderLayout.NORTH);

        JPanel diskCenterPanel = new JPanel(new GridLayout(2,1, 0,5));
        diskCenterPanel.add(diskUsageBar);
        diskCenterPanel.add(diskInfoLabel);

        diskPanel.add(diskCenterPanel, BorderLayout.CENTER);

        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                treePanel, diskPanel);
        verticalSplit.setDividerLocation(350);
        verticalSplit.setOneTouchExpandable(true);
        verticalSplit.setDividerSize(12);

        JLabel terminalLabel = new JLabel("Terminal", SwingConstants.CENTER);
        terminalLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane outputScroll = new JScrollPane(outputArea);

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandInput = new JTextField();
        commandInput.addActionListener(new ExecuteCommandListener());
        JButton executeButton = new JButton("Enter");
        executeButton.addActionListener(new ExecuteCommandListener());
        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);

        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.add(terminalLabel, BorderLayout.NORTH);
        terminalPanel.add(outputScroll, BorderLayout.CENTER);
        terminalPanel.add(commandPanel, BorderLayout.SOUTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                verticalSplit, terminalPanel);
        mainSplit.setDividerLocation(300);
        mainSplit.setOneTouchExpandable(true);
        mainSplit.setDividerSize(18);

        add(mainSplit, BorderLayout.CENTER);

        updateDiskUsageBar();
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
        JDialog popupDialog = new JDialog(this, a.getName(), true);
        popupDialog.setLayout(new BorderLayout());
        popupDialog.setSize(600, 500);
        popupDialog.setResizable(true);
        popupDialog.setLocationRelativeTo(this);

        final String[] originalContentRef = new String[] { a.getData() };

        JTextArea textArea = new JTextArea(a.getData());
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        popupDialog.add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        saveMenuItem.addActionListener(e -> {
            saveArquive(a, textArea.getText());
            originalContentRef[0] = textArea.getText();
        });

        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        popupDialog.setJMenuBar(menuBar);

        popupDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        popupDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                String currentContent = textArea.getText();
                if (!currentContent.equals(originalContentRef[0])) {
                    int result = JOptionPane.showConfirmDialog(
                            popupDialog,
                            "Do you want to save changes before closing?",
                            "Save File",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        saveArquive(a, currentContent);
                        popupDialog.dispose();
                    } else if (result == JOptionPane.NO_OPTION) {
                        popupDialog.dispose();
                    }
                } else {
                    popupDialog.dispose();
                }
            }
        });

        popupDialog.setVisible(true);
    }

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
                    CommandCatcher.getInstance().getResults().clear();
                    pipeline.execute(new CMD(command));

                    CommandCatcher catcher = CommandCatcher.getInstance();
                    ArquiveCatcher arquive = ArquiveCatcher.getInstance();

                    catcher.getResults().forEach((String result) -> {
                        outputArea.append(result + "\n");
                    });

                    if(arquive.getArquive() != null) {
                        arquivePopUp(arquive.getArquive());
                        ArquiveCatcher a = new ArquiveCatcher(null);
                    }

                    outputArea.setCaretPosition(outputArea.getDocument().getLength());

                    updateFileTree();
                } catch (UnexpectInputType | IOException | RuntimeException ex) {
                    outputArea.append("$ Error: " + command + " " + ex.getMessage() + "\n");
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                }

                commandInput.setText("");
            }

            commandInput.setText("");
        }
    }

    private void updateDiskUsageBar() {
        diskUsageBar.setProgress((double) 11 / 12);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem diskStatsItem = new JMenuItem("Disk Statistics");
        diskStatsItem.addActionListener(e -> showDiskUsagePopup());
        viewMenu.add(diskStatsItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
    }

    private void showDiskUsagePopup() {
        JDialog dialog = new JDialog(this, "Disk Details", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(this);

        CircularProgressBar bigBar = new CircularProgressBar();
        double used = getUsedClusters();
        double total = getTotalClusters();
        bigBar.setProgress(used / total);

        JLabel infoLabel = new JLabel(String.format("Used: %.2f / %.2f", used, total),
                SwingConstants.CENTER);

        dialog.add(bigBar, BorderLayout.CENTER);
        dialog.add(infoLabel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private double getUsedClusters() {
        return 10.00;
    }
    private double getTotalClusters() {
        return 11.00;
    }
}
