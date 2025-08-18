package org.example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Poll {
    private LinkedHashMap<String, List<String>> questions;
    private int delayTimeSeconds;
    private boolean pollReady;

    public Poll(int delayTimeMinutes) {
        this.delayTimeSeconds = delayTimeMinutes*60;
        if(delayTimeSeconds==0){
            pollReady = true;
        }
        else {
            pollReady=false;
            updateDelay();
        }
    }

    public boolean isPollReady() {
        return pollReady;
    }

    public LinkedHashMap<String, List<String>> getQuestions() {
        return questions;
    }

    private void updateDelay(){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if(delayTimeSeconds>0){
                delayTimeSeconds--;
                System.out.println("-");
            }
            else {
                pollReady = true;
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void setQuestions(LinkedHashMap<String, List<String>> questions) {
        this.questions = questions;
    }
}
