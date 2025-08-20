package org.example;

public class Option {
    private int votes;
    private String option;


    public int getVotes() {
        return votes;
    }

    public void addVote() {
        this.votes++;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return option;
    }
}
