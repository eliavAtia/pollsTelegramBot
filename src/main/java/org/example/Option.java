package org.example;

public class Option {
    private int votes;
    private String option;
    private Question question;


    public Option(String option,Question question) {
        this.option = option.trim();
        this.question = question;
        votes = 0;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void addVote() {
        this.votes++;
    }

    @Override
    public String toString() {
        return option;
    }

    public int percentageOfQuestion(){
        float ratio = (float) (votes) /(question.getAnsweredUsers().size());
        return (int) (ratio*100);
    }
}
