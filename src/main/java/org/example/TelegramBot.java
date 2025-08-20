package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot{
    private List<Long> usersChatIds;
    private boolean pollOn;
    private List<Poll> polls;
    private Poll currentPoll;

    //בנאי
    public TelegramBot() {
        this.usersChatIds = new ArrayList<>();
        this.polls = new ArrayList<>();
        this.pollOn = false;
        this.currentPoll=null;
        Poll poll = new Poll();
        // אופציות לשאלה 1
        Option red = new Option(); red.setOption("אדום");
        Option blue = new Option(); blue.setOption("כחול");
        Option green = new Option(); green.setOption("ירוק");
        Question q1 = new Question();
        q1.setQuestion("איזה צבע אתה אוהב?");
        q1.setOptions(Arrays.asList(red, blue, green));

        // אופציות לשאלה 2
        Option cat = new Option(); cat.setOption("חתול");
        Option dog = new Option(); dog.setOption("כלב");
        Option fish = new Option(); fish.setOption("דג");

        Question q2 = new Question();
        q2.setQuestion("איזה חיה אתה אוהב?");
        q2.setOptions(Arrays.asList(cat, dog, fish));

        // אופציות לשאלה 3
        Option pizza = new Option(); pizza.setOption("פיצה");
        Option burger = new Option(); burger.setOption("בורגר");
        Option salad = new Option(); salad.setOption("סלט");
        Question q3 = new Question();
        q3.setQuestion("מה הארוחה האהובה עליך?");
        q3.setOptions(Arrays.asList(pizza, burger, salad));

        // הוספת כל השאלות לסקר
        List<Question> questions = Arrays.asList(q1, q2, q3);
        poll.setQuestions(questions);

        // אפשר גם להגדיר זמן סיום לסקר אם רוצים
        poll.setDelayTimeSeconds(1); // לדוגמה 5 דקות
        poll.updateDelay();
        polls.add(poll);
        executePoll();
    }


    //פונקציות בסיס
    @Override
    public String getBotUsername() {
        return "@poll32_bot";
    }

    public String getBotToken(){
        return "8398683333:AAGgNSPpoz_0NhjnN-zw11O73D8MRvg-T8k";
    }


    //פונקצית onUpdateReceived
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            if(pollOn){
                checkAnswers(update);
            }
            else {
                sendMessage("The poll is already over",update.getMessage().getChatId());
            }
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

    private void sendMessageToEveryone(String message){
        for (int i = 0; i < usersChatIds.size(); i++) {
            sendMessage(message,usersChatIds.get(i));
        }
    }


    //פונקציות לבדיקת הודעה רגילה וצירוף לקהילה
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
            sendMessageToEveryone(name + " has joined the community, the community now contains " + usersChatIds.size() + " people.");
            usersChatIds.add(chatId);
        }
        else {
            sendMessage("To join the community please enter 'היי' or 'Hi' .",chatId);
        }

    }


    //פונקציות ליצירת סקר
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
                    if(currentPoll!=null){
                        sendAnswersOfPoll();
                        sendMessageToEveryone("The poll is over");
                        pollOn=false;
                        currentPoll=null;
                    }
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
        for(Question question : poll.getQuestions()){
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for(Option option: question.getOptions()){
                buttons.add(createButton(option,question.toString()));
            }
            List<List<InlineKeyboardButton>> keyBoard = new ArrayList<>();
            keyBoard.add(buttons);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyBoard);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(question.toString());
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            for (Long usersChatId : usersChatIds) {
                sendMessageWithPoll(sendMessage, usersChatId);
            }
        }
    }

    private InlineKeyboardButton createButton(Option option,String question){
        InlineKeyboardButton button = new InlineKeyboardButton(option.toString());
        button.setCallbackData(question+":"+option.toString());
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


    //פונקציות לבדיקת תשובות
    private void checkAnswers(Update update){
        CallbackQuery callback = update.getCallbackQuery();
        long chatId = callback.getFrom().getId();
        String data = callback.getData();
        String[] parts = data.split(":");
        String questionText = parts[0];
        String optionText = parts[1];
        Question question = currentPoll.getQuestions().stream()
                .filter(q -> q.toString().equals(questionText))
                .findFirst()
                .orElse(null);
        if(question != null) {
            boolean notVoted = question.addVote(optionText, chatId);
            if (!notVoted) {
                sendMessage("You have already voted, you cannot vote again.",chatId);
            }
        }
        checkIfPollOver();
    }

    private void checkIfPollOver(){
        boolean pollOver = true;
        for(Question question: currentPoll.getQuestions()){
            if (question.getAnsweredUsers().size() != usersChatIds.size()) {
                pollOver = false;
                break;
            }
        }
        if(pollOver){
            sendAnswersOfPoll();
            sendMessageToEveryone("The poll is over");
            pollOn=false;
            currentPoll=null;
        }
    }

    private void sendAnswersOfPoll(){

    }

}
