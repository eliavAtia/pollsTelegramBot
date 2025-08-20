package org.example;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GeneratingScreen extends JPanel {
    private Image image;
    private String topic;
    private boolean running=true;
    private JLabel label;
    private Poll currentPoll;
    private String pollInText;

    public GeneratingScreen(String topic,int x,int y,int width,int height){
        this.setBounds(x,y,width,height);
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.topic=topic;
        this.currentPoll = new Poll();
        labelBuilder();
        this.setLayout(null);
        runAnimation();
        restartChatGPT();
        createPoll();
        System.out.println(currentPoll);
    }

    private void restartChatGPT(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://app.seker.live/fm1/clear-history?id=327819769").build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String text = "שלום צאט התפקיד שלך זה ליצור סקר, אני אשלח לך נושא מסוים ואתה תיצור לי שאלות ואפשרויות שאפשר לענות עליהם. אתה תיצור 3 שאלות ולכל שאלה תיצור 3 תשובות אפשרויות שאפשר לענות עליה, למשל: שאלה- מה בצבע האהוב עליך. תשובות אפשרויות - כחולת אדום, צהוב. אתה תחזיר את השאלות והתשובות ככה, לפני כל שאלה תכתוב Q:: ואז תכתוב את השאלה עצמה, לאחר כל שאלה תכתוב את את כל התשובות שיש כאשר לפני על תשובה תכתוב O:: ואז התשובה עצמה. חייב שזה יעשה בדיוק ככה כי אני משתמש במתודת split בjava. דוגמא של סקר שאתה אמור להחזיר לי:\n" +
                "Q::מההצבע האהוב עליך?O::צהובO::כחולO::ירוקQ::מה החיה האהובה עליך?O::דולפיןO::כלבO::חתול. במקרה והנושא לא מובן תחזיר רק Error. אל תשיב על ההודעה הזאת. אני עכשיו אשלח לך הודעה שמכילה את הנושא.";
        System.out.println((String) sendToChatGpt(text).get("extra"));
        pollInText = (String) sendToChatGpt(topic).get("extra");
        System.out.println(pollInText);
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

    private void runAnimation(){
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

    public void stop() {
        running = false; // עוצר את הלופ
    }

    private void createPoll(){
        if(pollInText.trim().equals("Error")){
            stop();
            return;
        }
        String[] questions = pollInText.split("Q::");
        for (String qBlock : questions) {
            if (qBlock.isEmpty()) continue;
            String[] parts = qBlock.split("O::");
            Question question = new Question(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                question.addOption(parts[i]);
            }
            currentPoll.addQuestion(question);
            stop();
        }
    }

    public Poll getCurrentPoll() {
        return currentPoll;
    }
}
