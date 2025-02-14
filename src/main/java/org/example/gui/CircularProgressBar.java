package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class CircularProgressBar extends JComponent {

    private double progress = 0.0;

    public CircularProgressBar() {
        setPreferredSize(new Dimension(80, 80));
    }

    public void setProgress(double value) {
        progress = Math.max(0.0, Math.min(1.0, value));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()); // pega o menor lado
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(x, y, size, size);

        int angle = (int) Math.round(progress * 360);

        g2d.setColor(Color.GREEN);
        g2d.fillArc(x, y, size, size, 90, -angle);

        String text = String.format("%.0f%%", progress * 100);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth  = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int textX = getWidth()/2 - textWidth/2;
        int textY = getHeight()/2 + textHeight/2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(text, textX, textY);

        g2d.dispose();
    }
}
