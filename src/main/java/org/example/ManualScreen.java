package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
    private TelegramBot bot;
    private List<JTextArea> jTextAreaList;

    public ManualScreen(int x,int y,int width,int height,JFrame parentWindow,Poll poll,TelegramBot bot){
        this.setBounds(x,y,width,height);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setVisible(true);
        this.setLayout(null);
        labelsBuilder();
        textAreasBuilder();
        buttonsBuilder();
        this.bot = bot;
        this.parentWindow=parentWindow;
        this.poll=poll;
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
        jTextAreaList=new ArrayList<>();
        jTextAreaList.add(answer1);
        jTextAreaList.add(answer2);
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
            jTextAreaList.add(answer3);
            repaint();
            } else if (clicked==1) {
                this.add(answer4);
                jTextAreaList.add(answer4);
                repaint();
            }
            else {
                JOptionPane.showMessageDialog(parentWindow, " יכולות להיות עד ארבע תשובות אפשרויות.", "שגיאה", JOptionPane.ERROR_MESSAGE);
            }
            clicked++;
        });
        this.add(addButton);
        ImageButton addQuestionButton=new ImageButton("/Images/addQuestion.png");
        addQuestionButton.setBounds(getWidth()/2-180,260,200,120);
        this.add(addQuestionButton);
        addQuestionButton.addActionListener(e->{
            int num=buttonPressed(0);
            if (num==1){
                JOptionPane.showMessageDialog(parentWindow, "חובה להכניס לפחות שתי תשובות. ", "שגיאה", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (num==2){
                JOptionPane.showMessageDialog(parentWindow, "חובה לכתוב שאלה.", "שגיאה", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (num==3) {
                JOptionPane.showMessageDialog(parentWindow, "אפשר להכניס עד שלושה שאלות. ", "שגיאה", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ManualScreen manualScreen=new ManualScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,poll,bot);
            parentWindow.remove(this);
            parentWindow.add(manualScreen);
            parentWindow.revalidate();
            parentWindow.repaint();
        });
        ImageButton continueButton=new ImageButton("/Images/continue.png");
        continueButton.setBounds(getWidth()/2+10,250,145,145);
        continueButton.addActionListener(e->{
            if (poll.getQuestions()!=null&&poll.getQuestions().size()>=3){
                return;
            }
            int num=buttonPressed(1);
            if (num==1){
                JOptionPane.showMessageDialog(parentWindow, "חובה להכניס לפחות שתי תשובות. ", "שגיאה", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (num==2){
                JOptionPane.showMessageDialog(parentWindow, "חובה לכתוב שאלה.", "שגיאה", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PollReadyScreen pollReadyScreen=new PollReadyScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,poll,0,bot);
            parentWindow.remove(this);
            parentWindow.add(pollReadyScreen);
            parentWindow.revalidate();
            parentWindow.repaint();
        });
        this.add(continueButton);
    }

    public int buttonPressed(int num){
        Question question=new Question(questionArea.getText());
        String textQuestion =questionArea.getText();
        List<Option> optionList=new ArrayList<>();
        int answers=0;
        for (JTextArea jTextArea:jTextAreaList){
            String text=jTextArea.getText();
            if (text!=null  &&!text.trim().isEmpty()) {
                optionList.add(new Option(jTextArea.getText(),question));
                answers++;
            }
        }
        if(poll.getQuestions().size()>=2 && num==0){
            return 3;
        }
        if (questionArea.getText()==null|| textQuestion.trim().isEmpty()){
            return 2;
        }
        if (answers<2){
            return 1;
        }
        question.setOptions(optionList);
        poll.addQuestion(question);
        return 0;
    }
}
