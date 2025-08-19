package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot{
    private List<Long> usersChatIds;
    private boolean pollOn;
    private List<Poll> polls;
    private Poll currentPoll;


    public TelegramBot() {
        this.usersChatIds = new ArrayList<>();
        this.polls = new ArrayList<>();
        this.pollOn = false;
        LinkedHashMap<String, LinkedHashMap<String,Integer>> questions = new LinkedHashMap<>();
        questions.put("hello", new LinkedHashMap<>(Map.of("1", 1, "3", 3)));
        questions.put("hello2", new LinkedHashMap<>(Map.of("11", 11, "12", 12, "13", 13)));
        questions.put("hello3", new LinkedHashMap<>(Map.of("21", 21, "22", 22)));
        Poll poll = new Poll(1);
        poll.setQuestions(questions);
        polls.add(poll);
        executePoll();
    }

    @Override
    public String getBotUsername() {
        return "@poll32_bot";
    }

    public String getBotToken(){
        return "8398683333:AAGgNSPpoz_0NhjnN-zw11O73D8MRvg-T8k";
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            checkAnswers(update);
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            checkMessage(update);
        }
    }

    private void sendMessage(String message,long chatId){
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            execute(sendMessage);
        } catch ( TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAnswers(Update update){
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String answer = callbackQuery.getData();


    }


    private void checkMessage(Update update){
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        if(!usersChatIds.contains(chatId)){
            joinCommunity(update);
        }
        else {
            if(message.trim().equals("create poll")){
                addPoll(update);
            }
            else {
                sendMessage("The message is not understood, to create a poll please enter 'create poll' .",chatId);
            }
        }

    }

    private void joinCommunity(Update update){
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getForwardSenderName();
        if(update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("Hi") || update.getMessage().getText().equals("היי")){
            sendMessage("You have joined the polls bot community, to create a poll enter 'create poll'. ",chatId);
            for (int i = 0; i < usersChatIds.size(); i++) {
                sendMessage(name + " has joined the community, the community now contains " + usersChatIds.size() + " people.",usersChatIds.get(i));
            }
            usersChatIds.add(chatId);
        }
        else {
            sendMessage("To join the community please enter 'היי' or 'Hi' .",chatId);
        }

    }



    private void addPoll(Update update){
        long chatId = update.getMessage().getChatId();
        if(usersChatIds.size()<3){
            sendMessage("There are not enough people in the community.",chatId);
            return;
        }
        if(pollOn){
            sendMessage("There is a poll in progress, please wait until the poll is over.",chatId);
            return;
        }

    }



    private void executePoll(){
        new Thread(()->{
            while (true){
                for (Poll value : polls) {
                    if (value.isPollReady()) {
                        currentPoll = value;
                    }
                }
                polls.remove(currentPoll);
                if(currentPoll!=null){
                    createAndSendPoll(currentPoll);
                    pollOn = true;
                    sleep(50000);
                    currentPoll = null;
                }
                else {
                    sleep(1000);
                }
            }
        }).start();
    }

    private void sleep(int milliSeconds){
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAndSendPoll(Poll poll){
        Map<String, LinkedHashMap<String,Integer>> questions = poll.getQuestions();
        int questionNum = 1;
        for(Map.Entry<String, LinkedHashMap<String,Integer>> entry: questions.entrySet()){
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for(String option:entry.getValue().keySet()){
                buttons.add(createButton(option,questionNum));
            }
            List<List<InlineKeyboardButton>> keyBoard = new ArrayList<>();
            keyBoard.add(buttons);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyBoard);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(entry.getKey());
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            for (Long usersChatId : usersChatIds) {
                sendMessageWithPoll(sendMessage, usersChatId);
            }
            questionNum++;
        }
    }

    private InlineKeyboardButton createButton(String option, int questionNum){
        InlineKeyboardButton button = new InlineKeyboardButton(option);
        button.setCallbackData("Q"+questionNum+":"+option);
        return button;
    }

    private void sendMessageWithPoll(SendMessage sendMessage,long chatId){
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
