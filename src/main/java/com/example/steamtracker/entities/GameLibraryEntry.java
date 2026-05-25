package com.example.steamtracker.entities;

public class GameLibraryEntry {
    private Game game;
    private int playtime;
    private AchievementProgress achievements;


    public GameLibraryEntry(Game game, int playtime, AchievementProgress achievements) {
        this.game = game;
        this.playtime = playtime;
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

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public AchievementProgress getAchievements() {
        return achievements;
    }

    public void setAchievements(AchievementProgress achievements) {
        this.achievements = achievements;
    }
}
