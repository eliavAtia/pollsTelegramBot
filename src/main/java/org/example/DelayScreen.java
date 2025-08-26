package org.example;

import javax.swing.*;
import java.awt.*;

public class DelayScreen  extends JPanel {
    private Image image;
    private JFrame parentWindow;
    private JLabel label;
    private JTextArea minutesNumber;
    private Poll poll;
    private JSpinner spinner;
    private TelegramBot bot;

    public DelayScreen(int x,int y,int width,int height,JFrame parentWindow,Poll poll, TelegramBot bot){
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setLayout(null);
        this.poll=poll;
        this.bot = bot;
        this.parentWindow=parentWindow;
        labelBuilder();
        jtextAreaBuilder();
        buttonsBuilder();
    }
    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }
    public void labelBuilder(){
        label=new JLabel("<html>Would you like to launch your poll in delay? <br>" +
                "if so, choose in how many minutes it will be launched( use mousewheel)<br>"+"(leave it on 0 if you dont want to delay it).</html>");
        label.setForeground(Color.WHITE); // צבע טקסט
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(this.getWidth()/2-450,0,900,180);
        this.add(label);
    }
    public void jtextAreaBuilder(){
        SpinnerModel yearModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);
        ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().setEditable(false);
        yearSpinner.setBounds(getWidth()/2-50,getHeight()/2-50,100,100);
        yearSpinner.setFont(new Font("Arial",Font.BOLD,40));
        yearSpinner.addMouseWheelListener(e -> {
            int notches = e.getWheelRotation(); // +1 גלילה למטה, -1 גלילה למעלה
            int current = (Integer) yearSpinner.getValue();
            int step = (int) ((SpinnerNumberModel) yearSpinner.getModel()).getStepSize();
            int newValue = current + step * -notches; // הפוך את הכיוון אם רוצים
            SpinnerNumberModel model = (SpinnerNumberModel) yearSpinner.getModel();
            // לוודא שלא יוצא מהגבולות
            if (newValue >= (Integer) model.getMinimum() && newValue <= (Integer) model.getMaximum()) {
                yearSpinner.setValue(newValue);
            }
        });
        spinner=yearSpinner;
        this.add(yearSpinner);
    }
    public void buttonsBuilder(){
        ImageButton sendPoll=new ImageButton("/Images/sendPoll.png");
        sendPoll.setBounds(getWidth()/2-60,getHeight()/2+60,120,80);
        sendPoll.addActionListener(e -> {
            poll.setDelayTimeSeconds((int)spinner.getValue());
            bot.addPoll(poll);
            parentWindow.dispose();
        });
        this.add(sendPoll);
    }
}
