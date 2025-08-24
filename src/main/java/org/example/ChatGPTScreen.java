package org.example;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChatGPTScreen extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ChatGPTScreen.class);
    private Image image;
    private JLabel topic;
    private JTextArea topicArea;
    private JFrame parentWindow;
    private String pollInText;
    private TelegramBot bot;


    public ChatGPTScreen(int x,int y,int width,int height,JFrame parentWindow, TelegramBot bot){
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.setLayout(null);
        this.parentWindow=parentWindow;
        this.bot = bot;
        topicBuilder();
        texAreaBuilder();
        buttonsBuilder();
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    public void topicBuilder(){
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
            checksTopic();
        });
        this.add(continueButton);
    }

    public void checksTopic(){
        String text=topicArea.getText();
        if (topicArea.getText()==null||text.trim().isEmpty()){
            return;
        }
        restartChatGPT(text);
        Poll poll = createPoll();
        if(poll==null){
            JOptionPane.showMessageDialog(parentWindow, "לא ניתן ליצור סקר – הנושא לא מובן.", "שגיאה", JOptionPane.ERROR_MESSAGE);
            topicArea.setText("");
        }
        else {
            PollReadyScreen pollReadyScreen = new PollReadyScreen(getX(),getY(),getWidth(),getHeight(),parentWindow,poll,0,bot);
            parentWindow.remove(this);
            parentWindow.add(pollReadyScreen);
            parentWindow.revalidate();
            parentWindow.repaint();
        }
    }

    private void restartChatGPT(String topic){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://app.seker.live/fm1/clear-history?id=327819769").build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String text = "שלום צאט התפקיד שלך זה ליצור סקר, אני אשלח לך נושא מסוים ואתה תיצור לי שאלות ואפשרויות שאפשר לענות עליהם. אתה תיצור 3 שאלות ולכל שאלה תיצור 3 תשובות אפשרויות שאפשר לענות עליה, למשל: שאלה- מה בצבע האהוב עליך. תשובות אפשרויות - כחולת אדום, צהוב. אתה תחזיר את השאלות והתשובות ככה, לפני כל שאלה תכתוב Q:: ואז תכתוב את השאלה עצמה, לאחר כל שאלה תכתוב את את כל התשובות שיש כאשר לפני על תשובה תכתוב O:: ואז התשובה עצמה. חייב שזה יעשה בדיוק ככה כי אני משתמש במתודת split בjava. דוגמא של סקר שאתה אמור להחזיר לי:\n" +
                "Q::מההצבע האהוב עליך?O::צהובO::כחולO::ירוקQ::מה החיה האהובה עליך?O::דולפיןO::כלבO::חתול. במקרה והנושא לא מובן תחזיר רק Error. אל תשיב על ההודעה הזאת. אני עכשיו אשלח לך הודעה שמכילה את הנושא.";
        sendToChatGpt(text);
        pollInText = (String) sendToChatGpt(topic).get("extra");
    }

    private JSONObject sendToChatGpt(String string) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://app.seker.live/fm1/send-message?id=327819769&text="+string).build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Poll createPoll(){
        if(pollInText.trim().equals("Error")){
            return null;
        }
        Poll poll = new Poll();
        String[] questions = pollInText.split("Q::");
        for (String qBlock : questions) {
            if (qBlock.isEmpty()) continue;
            String[] parts = qBlock.split("O::");
            Question question = new Question(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                question.addOption(parts[i]);
            }
            poll.addQuestion(question);
        }
        return poll;
    }



}
