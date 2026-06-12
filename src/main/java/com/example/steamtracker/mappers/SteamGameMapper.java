package com.example.steamtracker.mappers;

import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.entities.Game;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.enums.Platform;
import com.example.steamtracker.models.GameStats;
import org.springframework.stereotype.Component;

@Component
public class SteamGameMapper {
    public GameLibraryEntry toGameLibrary(
            GameStats gameStats,
            AchievementProgress progress,
            GameStatus status
    ){
        Game game = new Game(gameStats.getAppId(), gameStats.getName(), Platform.STEAM);

        return new GameLibraryEntry(
                game,
                gameStats.getPlayTimeForever(),
                gameStats.getPlayTime2Weeks(),
                progress,
                status);
    }
}
