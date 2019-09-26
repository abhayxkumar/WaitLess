package com.example.hetavdesai.pl2project;

public class HighscoreClass {
    private String name;
    private String highscore;

    public HighscoreClass()
    {

    }

    public HighscoreClass(String name, String highscore) {
        this.name = name;
        this.highscore = highscore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHighscore() {
        return highscore;
    }

    public void setHighscore(String highscore) {
        this.highscore = highscore;
    }
}


