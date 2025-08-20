package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class ChatGPTScreen extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ChatGPTScreen.class);
    private Image image;
    private JLabel topic;
    private JTextArea topicArea;
    private JFrame parentWindow;


    public ChatGPTScreen(int x,int y,int width,int height,JFrame parentWindow){
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.setLayout(null);
        this.parentWindow=parentWindow;
        labelBuilder();
        texAreaBuilder();
        buttonsBuilder();
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    public void labelBuilder(){
        topic=new JLabel("Enter the desired topic: ");
        topic.setForeground(Color.WHITE); // צבע טקסט
        topic.setFont(new Font("Ariel", Font.BOLD, 30));
        topic.setHorizontalAlignment(SwingConstants.CENTER);
        topic.setBounds(getWidth()/2-175,25,350,100);
        this.add(topic);
    }

    public void texAreaBuilder(){
        topicArea=new JTextArea();
        topicArea=new JTextArea();
        topicArea.setFont(new Font("Aharoni", Font.PLAIN, 18));
        topicArea.setForeground(Color.black);// צבע טקסט
        topicArea.setBackground(Color.white);
        topicArea.setOpaque(true);// צבע הרקע
        topicArea.setBounds(getWidth()/2-100,150,200,20);
        this.add(topicArea);
    }

    public void buttonsBuilder(){
        ImageButton continueButton=new ImageButton("/Images/continue.png");
        continueButton.setBounds(getWidth()/2-100,180,200,200);
        continueButton.addActionListener(e->{
            bodek();
        });
        this.add(continueButton);
    }

    public void bodek(){
        String text=topicArea.getText();
        if (topicArea.getText()==null||text.trim().isEmpty()){
            return;
        }
        GeneratingScreen generatingScreen=new GeneratingScreen(topicArea.getText(),getX(),getY(),getWidth(),getHeight());
        parentWindow.remove(this);
        parentWindow.add(generatingScreen);
        parentWindow.revalidate();
        parentWindow.repaint();
    }
}
