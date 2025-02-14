package org.example.gui.panels;

import org.example.system.FileSystem;
import org.example.gui.components.FileSystemTreeRenderer;

import javax.swing.*;
import java.awt.*;

public class FileTreePanel extends JPanel {
    private final JTree fileTree;

    public FileTreePanel(FileSystem fileSystem) {
        setLayout(new BorderLayout());
        JLabel treeLabel = new JLabel("File System Tree", SwingConstants.CENTER);
        treeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(treeLabel, BorderLayout.NORTH);

        fileTree = new JTree(fileSystem.getFileSystemTree());
        fileTree.setCellRenderer(new FileSystemTreeRenderer());
        JScrollPane scrollPane = new JScrollPane(fileTree);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTree getFileTree() {
        return fileTree;
    }
}
