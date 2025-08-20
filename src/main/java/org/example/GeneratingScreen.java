package org.example;

import javax.swing.*;
import java.awt.*;

public class GeneratingScreen extends JPanel {
    private Image image;
    private String topic;
    private boolean running=true;
    private JLabel label;
    public GeneratingScreen(String topic,int x,int y,int width,int height){
        this.setBounds(x,y,width,height);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.topic=topic;
        labelBuilder();
        this.setLayout(null);
        Thread animationThread = new Thread(() -> {
            int dotCount = 0;
            while (running) {
                dotCount = (dotCount + 1) % 4; // בין 0 ל־3 נקודות
                StringBuilder dots = new StringBuilder();
                for (int i = 0; i < dotCount; i++) {
                    dots.append(".");
                }
                String text = "Generating" + dots;
                SwingUtilities.invokeLater(() -> label.setText(text));

                try {
                    Thread.sleep(500); // חצי שנייה
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        animationThread.start();
    }

    public void stop() {

        running = false; // עוצר את הלופ
    }
    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }
    public void labelBuilder(){
        label = new JLabel("Generating");
        label.setBounds(getWidth()/2-125,getHeight()/2-50,250,60);
        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setForeground(Color.white);
        add(label);
    }
}
