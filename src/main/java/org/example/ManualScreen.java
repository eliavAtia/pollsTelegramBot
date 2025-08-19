package org.example;

import javax.swing.*;
import java.awt.*;

public class ManualScreen extends JPanel {
    private Image image;
    private JTextArea questionArea;
    private JTextArea answer1;
    private JTextArea answer2;
    private JTextArea answer3;
    private JTextArea answer4;
    private JLabel questionText;
    private int currentAmountOfAnsweres;
    private int clicked=0;
    private Poll poll;
    private JFrame parentWindow;
    public ManualScreen(int x,int y,int width,int height,JFrame parentWindow){
        this.setBounds(x,y,width,height);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setVisible(true);
        this.setLayout(null);
        labelsBuilder();
        textAreasBuilder();
        buttonsBuilder();
        this.parentWindow=parentWindow;
    }
    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }
    public void textAreasBuilder(){
        questionArea=new JTextArea();
        questionArea.setFont(new Font("Aharoni", Font.PLAIN, 18));
        questionArea.setForeground(Color.black);// צבע טקסט
        questionArea.setBackground(Color.white);
        questionArea.setOpaque(true);// צבע הרקע
        questionArea.setBounds(getWidth()/2-250 ,50,500,40);
        add(questionArea);
        this.answer1=answerArea();
        this.answer2=answerArea();
        this.answer3=answerArea();
        this.answer4=answerArea();
        add(answer1);
        add(answer2);

    }
    public void labelsBuilder(){
        questionText=new JLabel("Enter your question: ");
        questionText.setForeground(Color.WHITE); // צבע טקסט
        questionText.setFont(new Font("Arial", Font.BOLD, 20));
        questionText.setHorizontalAlignment(SwingConstants.CENTER);
        questionText.setBounds(getWidth()/2-251,25,200,20);
        this.add(questionText);
        JLabel answersText=new JLabel("Enter the options: ");
        answersText.setForeground(Color.WHITE); // צבע טקסט
        answersText.setFont(new Font("Arial", Font.BOLD, 20));
        answersText.setHorizontalAlignment(SwingConstants.CENTER);
        answersText.setBounds(getWidth()/2-215,115,200,20);
        this.add(answersText);
    }
    public JTextArea answerArea(){
        currentAmountOfAnsweres++;
        JTextArea answerArea;
        answerArea=new JTextArea();
        answerArea.setFont(new Font("Aharoni", Font.PLAIN, 18));
        answerArea.setForeground(Color.black);// צבע טקסט
        answerArea.setBackground(Color.white);
        answerArea.setOpaque(true);// צבע הרקע
        answerArea.setBounds(getWidth()/2-200,100+40*currentAmountOfAnsweres,400,20);
        return answerArea;
    }
    public void buttonsBuilder(){
        ImageButton addButton =new ImageButton("/Images/add.png");
        addButton.setBounds(50,getHeight()/2-50,160,100);
        addButton.addActionListener(e -> {
            System.out.println(clicked);
            if (clicked==0){
            this.add(answer3);
            repaint();
            } else if (clicked==1) {
                this.add(answer4);
                repaint();
            }
            else {
                System.out.println("max amount of answers is 4");
            }
            clicked++;
        });
        this.add(addButton);
        ImageButton addQuestionButton=new ImageButton("/Images/addQuestion.png");
        addQuestionButton.setBounds(getWidth()/2-180,260,200,120);
        this.add(addQuestionButton);
        ImageButton continueButton=new ImageButton("/Images/continue.png");
        continueButton.setBounds(getWidth()/2+10,250,145,145);
        this.add(continueButton);
    }

}
