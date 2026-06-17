package com.example.steamtracker.entities;

import com.example.steamtracker.enums.CompletionTier;
import com.example.steamtracker.enums.GameStatus;
import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GameLibraryEntry {
    private Game game;
    private int playtimeForever;
    private int recentPlaytime;
    private AchievementProgress achievements;
    private GameStatus gameStatus;
    private CompletionTier completionTier;
}
