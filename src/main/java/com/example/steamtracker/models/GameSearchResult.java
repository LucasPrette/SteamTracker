package com.example.steamtracker.models;

public class GameSearchResult {
    private String gameName;
    private int appId;

    public GameSearchResult(int appId, String gameName) {
        this.appId = appId;
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public int getAppId() {
        return appId;
    }
}
