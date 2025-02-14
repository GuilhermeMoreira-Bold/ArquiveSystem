package org.example.gui.menu;

import org.example.gui.FileSystemGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileSystemMenuBar extends JMenuBar {
    public FileSystemMenuBar(FileSystemGUI parent) {
        super();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(exit -> System.exit(0));

        fileMenu.add(exitItem);
        add(fileMenu);

        JMenu viewMenu = new JMenu("View");
        JMenuItem diskStatsItem = new JMenuItem("Disk Statistics");

        diskStatsItem.addActionListener(e -> parent.showDiskUsageDialog());

        viewMenu.add(diskStatsItem);
        add(viewMenu);
    }
}
