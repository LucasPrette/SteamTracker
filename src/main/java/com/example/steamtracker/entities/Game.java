package com.example.steamtracker.entities;

import com.example.steamtracker.enums.Platform;

public class Game {
    private int externalID; // game identification
    private String gameName;
    private Platform platform;

    public Game(int externalID, String gameName, Platform platform) {
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

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}
