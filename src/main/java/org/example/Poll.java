package org.example;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Poll {
    private List<Question> questions;
    private int delayTimeSeconds;
    private boolean pollReady;


    public Poll() {
        questions=new ArrayList<>();
        pollReady=false;
    }

    public void setDelayTimeSeconds(int delayTimeMinutes) {
        this.delayTimeSeconds = delayTimeMinutes*60;
        if(this.delayTimeSeconds==0){
            pollReady=true;
        }
    }

    public void updateDelay(){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if(delayTimeSeconds>0){
                delayTimeSeconds--;
            }
            else {
                pollReady = true;
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isPollReady() {
        return pollReady;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Question question:questions){
            sb.append("Q: ").append(question).append("\n");
            for (Option option:question.getOptions()){
                sb.append("option: ").append(option).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
