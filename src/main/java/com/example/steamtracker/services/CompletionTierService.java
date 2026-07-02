package com.example.steamtracker.services;

import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.enums.CompletionTier;
import org.springframework.stereotype.Service;

@Service
public class CompletionTierService {

    public CompletionTier determineCompletion
            (
             AchievementProgress progress) {

        double percentage = progress.getCompletionPercentage();

        if (percentage <= 0) return CompletionTier.UNSTARTED;

        if(percentage < 40) return CompletionTier.IN_PROGRESS;

        if(percentage  < 75) return CompletionTier.STORY_CLEARED;

        if(percentage < 100) return CompletionTier.MASTERED;

        return CompletionTier.PERFECTED;
    }
}
