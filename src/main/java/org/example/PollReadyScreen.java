package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PollReadyScreen extends JPanel {
    private Image image;
    private JFrame parentWindow;
    private int index;
    private Question question;
    private JLabel questionText;
    private Poll poll;
    public PollReadyScreen(int x,int y,int width,int height,JFrame parentWindow,Poll poll,int index){
        this.setBounds(x,y,width,height);
        this.parentWindow=parentWindow;
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.question=poll.getQuestions().get(index);
        this.setLayout(null);
        this.poll=poll;
        labelBuilder();
        buttonBuilder();
    }
    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }
    public void labelBuilder(){
        questionText=new JLabel((index+1)+".  Question: "+question.getQuestion());
        questionText.setForeground(Color.lightGray); // צבע טקסט
        questionText.setFont(new Font("Arial", Font.BOLD, 30));
        questionText.setHorizontalAlignment(SwingConstants.LEFT);
        questionText.setBounds(100,25,700,40);
        this.add(questionText);
        for (int i = 0; i < question.getOptions().size(); i++) {
            JLabel answerText = new JLabel((i+1)+". "+question.getOptions().get(i));
            answerText.setForeground(Color.WHITE); // צבע טקסט
            answerText.setFont(new Font("Arial", Font.BOLD, 25));
            answerText.setHorizontalAlignment(SwingConstants.LEFT);
            answerText.setBounds(100,90+60*i,700,30);
            this.add(answerText);
        }
    }
    public void buttonBuilder(){
        ImageButton rightButton =new ImageButton("/Images/right.png");
        rightButton.setBounds(getWidth()/2+120,getHeight()/2+50,160,100);
        this.add(rightButton);
        ImageButton leftButton =new ImageButton("/Images/left (2).png");
        leftButton.setBounds(getWidth()/2-280,getHeight()/2+50,160,100);
        this.add(leftButton);
    }
}
