package com.example.steamtracker.models;

public class GamingStats {
    private int totalOwnedGames;

    private int backlogGames;
    private int playingGames;
    private int completedGames;
    private int abandonedGames;

    private int perfectedGames;
    private int masteredGames;
    private int storyClearedGames;
    private int inProgressGames;
    private int unstartedGames;

    private double completionRate;

    public GamingStats() {
    }

    public GamingStats(
            int totalOwnedGames,
            int backlogGames,
            int playingGames,
            int completedGames,
            int abandonedGames,
            int perfectedGames,
            int masteredGames,
            int storyClearedGames,
            int inProgressGames,
            int unstartedGames,
            double completionRate
    ) {
        this.totalOwnedGames = totalOwnedGames;
        this.backlogGames = backlogGames;
        this.playingGames = playingGames;
        this.completedGames = completedGames;
        this.abandonedGames = abandonedGames;
        this.perfectedGames = perfectedGames;
        this.masteredGames = masteredGames;
        this.storyClearedGames = storyClearedGames;
        this.inProgressGames = inProgressGames;
        this.unstartedGames = unstartedGames;
        this.completionRate = completionRate;
    }

    public int getTotalOwnedGames() {
        return totalOwnedGames;
    }

    public void setTotalOwnedGames(int totalOwnedGames) {
        this.totalOwnedGames = totalOwnedGames;
    }

    public int getBacklogGames() {
        return backlogGames;
    }

    public void setBacklogGames(int backlogGames) {
        this.backlogGames = backlogGames;
    }

    public int getPlayingGames() {
        return playingGames;
    }

    public void setPlayingGames(int playingGames) {
        this.playingGames = playingGames;
    }

    public int getCompletedGames() {
        return completedGames;
    }

    public void setCompletedGames(int completedGames) {
        this.completedGames = completedGames;
    }

    public int getAbandonedGames() {
        return abandonedGames;
    }

    public void setAbandonedGames(int abandonedGames) {
        this.abandonedGames = abandonedGames;
    }

    public int getPerfectedGames() {
        return perfectedGames;
    }

    public void setPerfectedGames(int perfectedGames) {
        this.perfectedGames = perfectedGames;
    }

    public int getMasteredGames() {
        return masteredGames;
    }

    public void setMasteredGames(int masteredGames) {
        this.masteredGames = masteredGames;
    }

    public int getStoryClearedGames() {
        return storyClearedGames;
    }

    public void setStoryClearedGames(int storyClearedGames) {
        this.storyClearedGames = storyClearedGames;
    }

    public int getInProgressGames() {
        return inProgressGames;
    }

    public void setInProgressGames(int inProgressGames) {
        this.inProgressGames = inProgressGames;
    }

    public int getUnstartedGames() {
        return unstartedGames;
    }

    public void setUnstartedGames(int unstartedGames) {
        this.unstartedGames = unstartedGames;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }
}
