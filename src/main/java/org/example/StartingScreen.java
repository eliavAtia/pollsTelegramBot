package org.example;

import javax.swing.*;
import java.awt.*;

public class StartingScreen extends JPanel {
    private Image image;
    private JLabel label;
    private JFrame parentWindow;
    private TelegramBot bot;

    public StartingScreen(int x,int y,int width,int height,JFrame parentWindow, TelegramBot bot){
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setLayout(null);
        this.parentWindow=parentWindow;
        this.bot = bot;
        labelBuilder();
        buttonBuilder();
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    public void buttonBuilder(){
        ImageButton manualImg=new ImageButton("/Images/Manual.png");
        manualImg.setBounds(200, 150, 120, 80); // x, y, width, height
        manualImg.addActionListener(e -> {
            Poll poll=new Poll();
            ManualScreen manualScreen=new ManualScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,poll,bot);
            parentWindow.remove(this);
            parentWindow.add(manualScreen);
            parentWindow.revalidate();
            parentWindow.repaint();
        });
        add(manualImg);

        ImageButton chatGptButton = new ImageButton("/Images/ChatGPT.png");
        chatGptButton.setBounds(400, 152, 120, 78); // x, y, width, height
        chatGptButton.addActionListener(e -> {
            ChatGPTScreen chatGPTScreen=new ChatGPTScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,bot);
            parentWindow.remove(this);
            parentWindow.add(chatGPTScreen);
            parentWindow.revalidate();
            parentWindow.repaint();
        });
        add(chatGptButton);
    }

    public void labelBuilder(){
        this.label=new JLabel("<html>Hi, would you like to create your poll manually?<br>" +
                "Or enter a topic and ChatGPT will generate a complete poll.</html>");
        label.setForeground(Color.WHITE); // צבע טקסט
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(this.getWidth()/5-30,0,500,180);
        this.add(label);
    }
}
