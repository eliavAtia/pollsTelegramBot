package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JFrame jFrame=new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(0,0,740,416);
//        jFrame.add(new StartingScreen(0,0,740,416,jFrame));
        Question question = new Question("מה אתה אוכל");
        question.setAnsweredUsers(new HashSet<>(Arrays.asList(10L, 20L, 30L, 40L)));
        question.addOption("פיצה");
        question.addOption("בורגר");
        question.addOption("ציפס");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < i; j++) {
                question.getOptions().get(i).addVote();
            }
        }
        System.out.println(question);
        for(Option option: question.getOptions()){
            System.out.println(option);
        }
        jFrame.add(new AnswersScreen(0,0,740,416,question));
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setResizable(false);

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}