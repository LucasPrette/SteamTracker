package com.example.steamtracker.models;
import lombok.AllArgsConstructor;
import lombok.Data;
    public class AchievementStats {
        private int unlocked;
        private int total;

        public AchievementStats(int unlocked, int total) {
            this.unlocked = unlocked;
            this.total = total;
        }

        public int getUnlocked() {
            return unlocked;
        }

        public int getTotal() {
            return total;
        }
    }

