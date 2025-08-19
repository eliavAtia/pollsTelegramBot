package org.example;

public class Option {
    private int votes;
    private String option;
    private int id;

    public Option(int id) {
        this.id = id;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote() {
        this.votes++;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getId() {
        return id;
    }
}
