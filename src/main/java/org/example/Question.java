package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question {
    private String question;
    private List<Option> options;
    private int id;
    private Set<Long> answeredUsers = new HashSet<>();


    public Question(int id) {
        this.id = id;
    }

    public boolean addVote(int optionId, long chatId) {
        if(answeredUsers.contains(chatId)){
            return false;
        }
        answeredUsers.add(chatId);
        Option option = options.stream()
                .filter(o -> o.getId() == optionId)
                .findFirst()
                .orElse(null);
        if(option != null){
            option.addVote();
        }
        return true;
    }

    public Set<Long> getAnsweredUsers() {
        return answeredUsers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

}
