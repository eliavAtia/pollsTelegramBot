package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PollReadyScreen extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(PollReadyScreen.class);
    private Image image;
    private JFrame parentWindow;
    private int index;
    private Question question;
    private JLabel questionText;
    private Poll poll;
    private TelegramBot bot;
    private String rightUrl;
    private String leftUrl;


    public PollReadyScreen(int x,int y,int width,int height,JFrame parentWindow,Poll poll,int index,TelegramBot bot){
        this.setBounds(x,y,width,height);
        this.parentWindow=parentWindow;
        this.index=index;
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.question=poll.getQuestions().get(this.index);
        this.setLayout(null);
        this.poll=poll;
        this.bot = bot;
        labelBuilder();
        urlBuilder();
        buttonBuilder();
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    private void labelBuilder(){
        questionText=new JLabel((index+1)+". "+question.getQuestion());
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

    private void buttonBuilder(){
        ImageButton rightButton =new ImageButton(rightUrl);
        rightButton.setBounds(getWidth()/2+120,getHeight()/2+50,160,100);
        rightButton.addActionListener(e -> {
            rightButtonClicked();
        });
        this.add(rightButton);
        ImageButton leftButton =new ImageButton(leftUrl);
        leftButton.setBounds(getWidth()/2-280,getHeight()/2+50,160,100);
        leftButton.addActionListener(e -> {
            leftButtonClicked();
        });
        this.add(leftButton);
        ImageButton createNew=new ImageButton("/Images/createNew.png");
        createNew.setBounds(getWidth()/2-180,getHeight()/2+40,200,120);
        createNew.addActionListener(e->{
            StartingScreen startingScreen=new StartingScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,bot);
            parentWindow.remove(this);
            parentWindow.add(startingScreen);
            parentWindow.repaint();
            parentWindow.revalidate();
        });
        this.add(createNew);
        ImageButton continueButton=new ImageButton("/Images/continue.png");
        continueButton.setBounds(getWidth()/2-20,getHeight()/2,180,200);
        this.add(continueButton);
    }

    private void rightButtonClicked(){
        if (index>=poll.getQuestions().size()-1){
            return;
        }
        parentWindow.remove(this);
        parentWindow.add(new PollReadyScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,poll,index+1,bot));
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    private void leftButtonClicked(){
        if (index<=0){
            return;
        }
        parentWindow.remove(this);
        parentWindow.add(new PollReadyScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,poll,index-1,bot));
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    private void urlBuilder(){
        if (index<=0){
            leftUrl="/Images/grayLeft.png";
        }
        else {
            leftUrl="/Images/left (2).png";
        }
        if (index>=poll.getQuestions().size()-1){
            rightUrl="/images/grayRight.png";
        }
        else {
            rightUrl="/Images/right.png";
        }
    }
}
