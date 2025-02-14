package org.example.gui.dialogs;

import org.example.system.arquives.Arquive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class ArquiveDialog extends JDialog {

    private final Consumer<String> onSaveCallback;

    private final JTextArea textArea;
    private String originalContent;

    public ArquiveDialog(Frame parent, Arquive a, Consumer<String> onSaveCallback) {
        super(parent, a.getName(), true);
        this.onSaveCallback = onSaveCallback;
        this.originalContent = a.getData();

        setLayout(new BorderLayout());
        setSize(600, 500);
        setResizable(true);
        setLocationRelativeTo(parent);

        textArea = new JTextArea(a.getData());
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            doSave();
        });
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String currentContent = textArea.getText();
                if (!currentContent.equals(originalContent)) {
                    int result = JOptionPane.showConfirmDialog(
                            ArquiveDialog.this,
                            "Do you want to save changes before closing?",
                            "Save File",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        doSave();
                        dispose();
                    } else if (result == JOptionPane.NO_OPTION) {
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
    }

    private void doSave() {
        String newContent = textArea.getText();
        onSaveCallback.accept(newContent);
        originalContent = newContent;
    }
}
