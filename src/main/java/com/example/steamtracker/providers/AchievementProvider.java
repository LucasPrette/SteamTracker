package com.example.steamtracker.providers;

import com.example.steamtracker.entities.AchievementProgress;

public interface AchievementProvider {
    AchievementProgress getAchievements(int externalId);
}
