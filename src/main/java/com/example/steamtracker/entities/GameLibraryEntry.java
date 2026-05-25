package com.example.steamtracker.entities;

public class GameLibraryEntry {
    private Game game;
    private int playtimeForever;
    private int recentPlaytime;
    private AchievementProgress achievements;


    public GameLibraryEntry(Game game, int playtimeForever, int recentPlaytime, AchievementProgress achievements) {
        this.game = game;
        this.playtimeForever = playtimeForever;
        this.recentPlaytime = recentPlaytime;
        this.achievements = achievements;
    }

    public GameLibraryEntry() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getPlaytimeForever() {
        return playtimeForever;
    }

    public void setPlaytimeForever(int playtime) {
        this.playtimeForever = playtime;
    }

    public AchievementProgress getAchievements() {
        return achievements;
    }

    public void setAchievements(AchievementProgress achievements) {
        this.achievements = achievements;
    }

    public int getRecentPlaytime() {
        return recentPlaytime;
    }

    public void setRecentPlaytime(int recentPlaytime) {
        this.recentPlaytime = recentPlaytime;
    }
}
