package org.example.gui;

import org.example.system.FileSystem;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class FileSystemTreeRenderer extends DefaultTreeCellRenderer {

    private final Icon folderIcon = UIManager.getIcon("FileView.directoryIcon");
    private final Icon fileIcon = UIManager.getIcon("FileView.fileIcon");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof FileSystem.FileSystemTreeNode node) {
            if (node.isDirectory()) {
                setIcon(folderIcon);
            } else {
                setIcon(fileIcon);
            }
        }

        return this;
    }

}
