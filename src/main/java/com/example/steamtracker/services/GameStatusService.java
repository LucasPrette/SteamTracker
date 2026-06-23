package com.example.steamtracker.services;

import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.enums.CompletionTier;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.models.GameStats;
import org.springframework.stereotype.Service;

@Service
public class GameStatusService {

    public GameStatus determineStatus (
            GameStats game,
            CompletionTier completionTier,
            Boolean recentlyPlayed
    ) {
        if(recentlyPlayed) {
            return GameStatus.PLAYING;
        }

        if(completionTier == CompletionTier.STORY_CLEARED
                || completionTier == CompletionTier.MASTERED
                || completionTier == CompletionTier.PERFECTED) {

            return GameStatus.COMPLETED;
        }

        if(game.getPlayTimeForever() < 2) {
            return GameStatus.BACKLOG;
        }

        return GameStatus.ABANDONED;
    }
}
