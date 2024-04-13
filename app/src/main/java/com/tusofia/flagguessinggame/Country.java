package com.tusofia.flagguessinggame;

public class Country {
    private String name;
    private String flagUrl;
    private String difficulty;

    public Country(String name, String flagUrl, String difficulty) {
        this.name = name;
        this.flagUrl = flagUrl;
        this.difficulty = difficulty;
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
