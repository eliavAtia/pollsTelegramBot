package org.example;

import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {
    private Image image;

    public ImageButton(String url) {
        this.image = new ImageIcon(getClass().getResource(url)).getImage();
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
