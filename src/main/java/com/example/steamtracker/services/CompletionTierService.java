package com.example.steamtracker.services;

import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.enums.CompletionTier;
import com.example.steamtracker.models.GameStats;
import org.springframework.stereotype.Service;

@Service
public class CompletionTierService {

    public CompletionTier determineCompletion
            (
             AchievementProgress progress) {

        if (progress.getCompletionPercentage() == 0) return CompletionTier.UNSTARTED;

        if(progress.getCompletionPercentage() > 1 && progress.getCompletionPercentage() <= 39) {
            return CompletionTier.IN_PROGRESS;
        }

        if(progress.getCompletionPercentage() > 40
                && progress.getCompletionPercentage() <= 74) {
            return CompletionTier.STORY_CLEARED;
        }

        if(progress.getCompletionPercentage() > 75
                && progress.getCompletionPercentage() <= 99) {
            return CompletionTier.MASTERED;
        }

        return CompletionTier.PERFECTED;
    }
}
