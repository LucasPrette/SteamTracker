package com.example.steamtracker.models;

public class GamingAdvice {
    private String action;
    private String gameName;
    private String reason;

    public GamingAdvice() {
    }

    public GamingAdvice(String action, String gameName, String reason) {
        this.action = action;
        this.gameName = gameName;
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
