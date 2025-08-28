package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        TelegramBot bot = new TelegramBot();
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame startFrame = new JFrame("ניהול סקרים");
            startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            startFrame.setSize(300, 150);
            startFrame.getContentPane().setBackground(Color.BLUE);
            startFrame.setLayout(null);
            startFrame.setResizable(false);
            startFrame.setLocationRelativeTo(null);
            JButton createPollBtn = new JButton("צור סקר חדש");
            createPollBtn.setBounds(75,25,150,50);
            createPollBtn.setFont(new Font("Arial", Font.BOLD, 20));
            createPollBtn.addActionListener(e -> {
                int result = bot.canAddPoll();
                result = 0;
                if (result == 1) {
                    JOptionPane.showMessageDialog(startFrame, "לא ניתן ליצור סקר – יש פחות משלושה משתמשים רשומים.", "שגיאה", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (result == 2) {
                    JOptionPane.showMessageDialog(startFrame, "לא ניתן ליצור סקר – כבר קיים סקר פעיל.", "שגיאה", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    JFrame pollFrame = new JFrame("יצירת סקר");
                    pollFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    pollFrame.setSize(740, 416);
                    pollFrame.setLayout(null);
                    pollFrame.setLocationRelativeTo(null);
                    pollFrame.add(new StartingScreen(0, 0, 740, 416,pollFrame ,bot));
                    pollFrame.setVisible(true);
                    pollFrame.setResizable(false);
                }
            });
            startFrame.add(createPollBtn);
            startFrame.setVisible(true);
        });
    }

}