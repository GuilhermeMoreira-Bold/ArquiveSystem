package org.example.gui.panels;

import javax.swing.*;
import java.awt.*;

public class TerminalPanel extends JPanel {
    private final JTextArea outputArea;
    private final JTextField commandInput;
    private final JButton executeButton;

    public TerminalPanel() {
        setLayout(new BorderLayout());

        JLabel terminalLabel = new JLabel("Terminal", SwingConstants.CENTER);
        terminalLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(terminalLabel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        add(outputScroll, BorderLayout.CENTER);

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandInput = new JTextField();
        executeButton = new JButton("Enter");
        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);
        add(commandPanel, BorderLayout.SOUTH);
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }

    public JTextField getCommandInput() {
        return commandInput;
    }

    public JButton getExecuteButton() {
        return executeButton;
    }
}
