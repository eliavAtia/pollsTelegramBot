package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question {
    private String question;
    private List<Option> options;
    private Set<Long> answeredUsers = new HashSet<>();


    public Question(String question) {
        this.question = question;
        this.options = new ArrayList<>();
    }

    public boolean addVote(String optionText, long chatId) {
        if(answeredUsers.contains(chatId)){
            return false;
        }
        answeredUsers.add(chatId);
        Option option = options.stream()
                .filter(o -> o.toString().equals(optionText))
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return question;
    }

    public void addOption(String optionText){
        Option option = new Option(optionText);
        option.setQuestion(this);
        options.add(option);
    }

    public void setAnsweredUsers(Set<Long> answeredUsers) {
        this.answeredUsers = answeredUsers;
    }
    public String getQuestion(){
        return this.question;
    }
}
