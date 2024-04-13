package com.tusofia.flagguessinggame.entity;

public class Country {
    private String difficulty;
    private String flagUrl;
    private String name;

    public Country() {
    }

    public Country(String difficulty, String flagUrl, String name) {
        this.difficulty = difficulty;
        this.flagUrl = flagUrl;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
