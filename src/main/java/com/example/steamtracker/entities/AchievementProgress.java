package com.example.steamtracker.entities;

public class AchievementProgress {
    private int unlocked;
    private int total;

    public AchievementProgress() {
    }

    public AchievementProgress(int unlocked, int total) {
        this.unlocked = unlocked;
        this.total = total;
    }

    public int getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(int unlocked) {
        this.unlocked = unlocked;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getCompletionPercentage() {

        if (total == 0) {
            return 0;
        }

        return ((double) unlocked / total) * 100;
    }
}
