package com.tusofia.flagguessinggame.entity;

public class LeaderboardEntry {
    private int position;
    private String name;
    private long highScore;

    public LeaderboardEntry(int position, String name, long highScore) {
        this.position = position;
        this.name = name;
        this.highScore = highScore;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public long getHighScore() {
        return highScore;
    }
}

