package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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


    public TelegramBot() {
        this.usersChatIds = new ArrayList<>();
        this.polls = new ArrayList<>();
        this.pollOn = false;
        LinkedHashMap<String, List<String>> questions = new LinkedHashMap<>();
        questions.put("hello",List.of("1","2","3","3"));
        questions.put("hello2",List.of("11","12","13"));
        questions.put("hello3",List.of("21","22","23"));
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
        checkMessage(update);

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
                Poll poll = null;
                for (Poll value : polls) {
                    if (value.isPollReady()) {
                        poll = value;
                    }
                }
                polls.remove(poll);
                if(poll!=null){
                    createAndSendPoll(poll);
                    pollOn = true;
                    sleep(50000);
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
        Map<String, List<String>> questions = poll.getQuestions();
        int questionNum = 1;
        for(Map.Entry<String,List<String>> entry: questions.entrySet()){
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for(String option:entry.getValue()){
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
        button.setCallbackData("q"+questionNum+":;"+
                option);
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
