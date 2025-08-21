package org.example;

public class Option {
    private int votes;
    private String option;
    private Question question;


    public Option(String option) {
        this.option = option;
        votes = 0;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getVotes() {
        return votes;
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
