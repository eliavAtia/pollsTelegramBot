package org.example;

import javax.swing.*;
import java.awt.*;

public class DelayScreen  extends JPanel {
    private Image image;
    private JFrame parentWindow;
    private JLabel label;
    private JTextArea minutesNumber;
    private Poll poll;
    public DelayScreen(int x,int y,int width,int height,JFrame parentWindow,Poll poll){
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setLayout(null);
        this.poll=poll;
        labelBuilder();
        jtextAreaBuilder();
    }
    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }
    public void labelBuilder(){
        label=new JLabel("Would you like to launch your poll in delay? ");
        label.setForeground(Color.WHITE); // צבע טקסט
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(this.getWidth()/2-300,0,600,180);
        this.add(label);
    }
    public void jtextAreaBuilder(){
        minutesNumber=new JTextArea();
        minutesNumber=new JTextArea();
        minutesNumber.setFont(new Font("Aharoni", Font.PLAIN, 20));
        minutesNumber.setForeground(Color.black);// צבע טקסט
        minutesNumber.setBackground(Color.white);
        minutesNumber.setOpaque(true);// צבע הרקע
        minutesNumber.setBounds(getWidth()/2-50,150,100,20);
        this.add(minutesNumber);
    }
}
