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
        this.question = question.trim();
        this.options = new ArrayList<>();
    }

    public boolean addVote(int optionIndex, long chatId) {
        if(answeredUsers.contains(chatId)){
            return false;
        }
        answeredUsers.add(chatId);
        if (optionIndex >= 0 && optionIndex < options.size()) {
            Option option = options.get(optionIndex);
            option.addVote();
            return true;
        }
        return false;
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
        Option option = new Option(optionText,this);
        options.add(option);
    }

    public String getQuestion(){
        return this.question;
    }
}
