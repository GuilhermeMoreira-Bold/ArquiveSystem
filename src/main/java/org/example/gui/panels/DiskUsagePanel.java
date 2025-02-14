package org.example.gui.panels;

import org.example.gui.components.CircularProgressBar;
import org.example.system.disk.VirtualDisk;

import javax.swing.*;
import java.awt.*;

import static org.example.system.disk.DiskUtils.CLUSTER_SIZE;

public class DiskUsagePanel extends JPanel {

    private final VirtualDisk disk;
    private final CircularProgressBar progressBar;
    private final JLabel infoLabel;

    public DiskUsagePanel(VirtualDisk disk) {
        this.disk = disk;
        this.progressBar = new CircularProgressBar();
        this.infoLabel = new JLabel("", SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);

        updateDiskInfo();
    }

    public void updateDiskInfo() {
        int usedClusters = disk.getUsedClusters();
        int totalClusters = disk.getTotalClusters();
        int freeClusters = totalClusters - usedClusters;

        long usedBytes  = (long) usedClusters * CLUSTER_SIZE;
        long totalBytes = (long) totalClusters * CLUSTER_SIZE;
        long freeBytes  = (long) freeClusters * CLUSTER_SIZE;

        double fraction = (double) usedBytes / totalBytes;
        progressBar.setProgress(fraction);

        String usedHR  = humanReadableSize(usedBytes);
        String totalHR = humanReadableSize(totalBytes);
        String freeHR  = humanReadableSize(freeBytes);

        String labelText = String.format(
                "<html>" +
                        "<div style='text-align:center;'>" +
                        "Used: %s / %s (%.0f%%)<br>" +
                        "Available: %s" +
                        "</div>" +
                        "</html>",
                usedHR, totalHR, fraction * 100, freeHR
        );
        infoLabel.setText(labelText);
    }

    private String humanReadableSize(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
