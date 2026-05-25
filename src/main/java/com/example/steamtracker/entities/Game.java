package com.example.steamtracker.entities;

public class Game {
    private int externalID; // game identification
    private String gameName;
    private String platform;

    public Game(int externalID, String gameName, String platform) {
        this.externalID = externalID;
        this.gameName = gameName;
        this.platform = platform;
    }

    public Game() {
    }

    public int getExternalID() {
        return externalID;
    }

    public void setExternalID(int externalID) {
        this.externalID = externalID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
