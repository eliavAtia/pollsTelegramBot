package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import java.util.*;
import java.util.List;

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


    //פונקציות onUpdateReceived
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            if(pollOn){
                checkAnswers(update);
            }
            else {
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                sendMessage("The poll is already over", chatId);
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
            sendMessage.setParseMode("MarkdownV2");
            execute(sendMessage);
        } catch ( TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessageToEveryone(String message){
        for (Long usersChatId : usersChatIds) {
            sendMessage(message, usersChatId);
        }
    }


    //פונקציות לבדיקת הודעה רגילה וצירוף לקהילה
    private void checkMessage(Update update){
        long chatId = update.getMessage().getChatId();
        if(!usersChatIds.contains(chatId)){
            joinCommunity(update);
        }
        else {
            sendMessage("*❌ Error:*\n" +
                    "You cannot send messages to the bot\\. \n" +
                    "You can only _vote in polls_\\.",chatId);
        }

    }

    private void joinCommunity(Update update){
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getFrom().getFirstName();
        if(update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("Hi") || update.getMessage().getText().equals("היי")){
            sendMessage("*🎉 Welcome\\!* \n" +
                    "You have joined the *poll bot community* ✅\n\n" +
                    "From now on, polls will be sent to you and you can vote\\.", chatId);
            sendMessageToEveryone("👤 *" + escape(name) + "* has joined the community\\! 🎉\\n" +
                    "The community now contains *" + (usersChatIds.size() + 1) + "* people 👥");
            usersChatIds.add(chatId);
        }
        else {
            sendMessage("⚠️ To join the community please type *'היי'* or *'Hi'*",chatId);
        }

    }

    public String escape(String text){
        if(text == null) return "";
        return text
                .replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }


    //פונקציות להרצת סקר
    private void executePoll(){
        new Thread(()->{
            while (true){
                if(!pollOn){
                    searchPoll();
                }
                if (pollOn) {
                    runPoll();
                    sleep(3000);
                }
            }
        }).start();
    }

    private void searchPoll(){
        for (Poll value : polls) {
            if (value.isPollReady()) {
                currentPoll = value;
                break;
            }
        }
        if(currentPoll!=null){
            polls.remove(currentPoll);
            createAndSendPoll(currentPoll);
            pollOn = true;
        }
        else {
            sleep(1000);
        }
    }

    private void runPoll(){
        long startTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - startTime >= 300_000) {
                String text = "*⏰ 5 minutes have passed*\n_The poll has closed\\._";
                sendMessageToEveryone(text);
                break;
            }
            if (checkIfPollOver()) {
                String text = "*✅ Everyone voted\\!*\n_The poll is over\\._";
                sendMessageToEveryone(text);
                break;
            }
            sleep(100);
        }
        Poll pollToShow = currentPoll;
        sendAnswersOfPoll(pollToShow);
        pollOn = false;
        currentPoll = null;
    }

    private void sleep(int milliSeconds){
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    //פונקציות ליצירת סקר
    public void addPoll(Poll poll){
        poll.updateDelay();
        this.polls.add(poll);
    }

    public int canAddPoll(){
        if(this.usersChatIds.size()<2){
            return 1;
        }
        else if(pollOn){
            return 2;
        }
        return 0;
    }

    public void createAndSendPoll(Poll poll){
        sendMessageToEveryone("📊 A new poll has been sent\\! Please answer all questions within 5 minutes\\.");
        for(Question question : poll.getQuestions()){
            for (Long usersChatId : usersChatIds) {
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                for(Option option: question.getOptions()){
                    buttons.add(createButton(option, question));
                }

                List<List<InlineKeyboardButton>> keyBoard = new ArrayList<>();
                keyBoard.add(buttons);
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(keyBoard);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(usersChatId);
                sendMessage.setText(question.toString());
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                sendMessageWithPoll(sendMessage, usersChatId);
            }
        }
    }

    private InlineKeyboardButton createButton(Option option, Question question){
        InlineKeyboardButton button = new InlineKeyboardButton(option.toString());
        int questionIndex = currentPoll.getQuestions().indexOf(question);
        int optionIndex = question.getOptions().indexOf(option);
        button.setCallbackData(questionIndex + "_" + optionIndex);
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
        int messageId =callback.getMessage().getMessageId();
        String data = callback.getData();
        String[] parts = data.split("_");
        int questionIndex = Integer.parseInt(parts[0]);
        int optionIndex = Integer.parseInt(parts[1]);
        Question question = null;
        if (questionIndex >= 0 && questionIndex < currentPoll.getQuestions().size()) {
            question = currentPoll.getQuestions().get(questionIndex);
        }
        if(question != null) {
            boolean notVoted = question.addVote(optionIndex, chatId);
            if (!notVoted) {
                sendMessage("You have already voted, you cannot vote again.",chatId);
            }
        }
        int numOfQuestion = questionIndex+1;
        sendMessage("✅ *Answer received\\!* for question " + numOfQuestion, chatId);
        deleteMessage(chatId,messageId);
    }

    private void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(chatId));
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfPollOver(){
        for(Question question: currentPoll.getQuestions()){
            if (question.getAnsweredUsers().size() != usersChatIds.size()) {
                return false;
            }
        }
        return true;
    }

    private void sendAnswersOfPoll(Poll poll){
        SwingUtilities.invokeLater(() -> {
            JFrame jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jFrame.setBounds(0, 0, 740, 416);
            jFrame.setLayout(null);
            jFrame.add(new AnswersScreen(0,0,740,416, poll, jFrame,0));
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
            jFrame.setResizable(false);
        });
    }
}
