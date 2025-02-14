package org.example.gui.dialogs;

import org.example.gui.components.CircularProgressBar;
import org.example.system.disk.VirtualDisk;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal que exibe detalhes sobre o disco,
 * com duas abas: "Overview" (barra circular) e "Details" (blocos coloridos).
 */
public class DiskUsageDialog extends JDialog {

    private final VirtualDisk disk;

    public DiskUsageDialog(Frame parent, VirtualDisk disk) {
        super(parent, "Disk Details", true);
        this.disk = disk;

        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel overviewPanel = createOverviewPanel();
        tabbedPane.addTab("Overview", overviewPanel);

        JPanel detailsPanel = createDetailsPanel();
        tabbedPane.addTab("Details", detailsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        CircularProgressBar bigBar = new CircularProgressBar();

        int usedClusters = disk.getUsedClusters();
        int totalClusters = disk.getTotalClusters();
        int freeClusters = totalClusters - usedClusters;

        long usedBytes = (long) usedClusters * disk.getClusterSize();
        long totalBytes = (long) totalClusters * disk.getClusterSize();
        long freeBytes  = (long) freeClusters  * disk.getClusterSize();

        double fraction = usedBytes / (double) totalBytes;
        bigBar.setProgress(fraction);

        String usedHR  = humanReadableSize(usedBytes);
        String totalHR = humanReadableSize(totalBytes);
        String freeHR  = humanReadableSize(freeBytes);

        JLabel infoLabel = new JLabel(
                String.format(
                        "<html>" +
                                "<table border='0' cellpadding='6' style='text-align:center; font-size:10px;'>"
                                + "<tr><td>Used:</td><td>%s / %s (%.0f%%)</td></tr>"
                                + "<tr><td>Free:</td><td>%s</td></tr>"
                                + "</table>"
                                + "</html>",
                        usedHR, totalHR, fraction * 100, freeHR
                ),
                SwingConstants.CENTER
        );

        panel.add(bigBar, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel summaryLabel = createClusterSummaryLabel();
        panel.add(summaryLabel, BorderLayout.NORTH);

        int[] fileClusters = disk.getFileClustersArray();
        int total = fileClusters.length;

        int columns = 25;
        int rows = (int) Math.ceil((double) total / columns);

        JPanel blocksPanel = new JPanel(new GridLayout(rows, columns, 5, 5));

        for (int i = 0; i < total; i++) {
            int status = fileClusters[i];
            blocksPanel.add(createClusterBlock(i, status));
        }

        JScrollPane scrollPane = new JScrollPane(blocksPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static String humanReadableSize(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private Component createClusterBlock(int index, int status) {
        JPanel block = new JPanel();
        block.setPreferredSize(new Dimension(10, 10));

        if (status == -1) {
            block.setBackground(Color.GREEN);
            block.setToolTipText("Cluster " + index + ": FREE");
        } else if (status == -2) {
            block.setBackground(Color.RED);
            block.setToolTipText("Cluster " + index + ": EOF");
        } else {
            block.setBackground(Color.BLUE);
            block.setToolTipText("Cluster " + index + ": Used => next = " + status);
        }

        block.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return block;
    }

    private JLabel createClusterSummaryLabel() {
        int used = disk.getUsedClusters();
        int total = disk.getTotalClusters();
        int free = total - used;

        String text = String.format(
                "<html>" +
                        "<table border='0' cellpadding='6' style='text-align:center; font-size:10px;'>" +
                        "Total Clusters: %d <br>" +
                        "Used Clusters: %d <br>" +
                        "Free Clusters: %d <br>" +
                        "</table>" +
                "</html>",
                total, used, free
        );

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }
}
