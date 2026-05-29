package com.example.steamtracker.mappers;

import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.entities.Game;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.Platform;
import com.example.steamtracker.models.AchievementStats;
import com.example.steamtracker.models.GameStats;
import org.springframework.stereotype.Component;

@Component
public class SteamGameMapper {
    public GameLibraryEntry toGameLibrary(
            GameStats gameStats,
            AchievementStats achievementStats
    ){
        Game game = new Game(gameStats.getAppId(), gameStats.getName(), Platform.STEAM);

        AchievementProgress progress = new AchievementProgress(
                achievementStats.getUnlocked(),
                achievementStats.getTotal()
        );

        return new GameLibraryEntry(
                game,
                gameStats.getPlayTimeForever(),
                gameStats.getPlayTime2Weeks(),
                progress);
    }
}
