package com.example.steamtracker.providers;

import com.example.steamtracker.clients.SteamClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.mappers.SteamGameMapper;
import com.example.steamtracker.models.AchievementStats;
import com.example.steamtracker.models.GameStats;
import com.example.steamtracker.services.SteamService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SteamLibraryProvider implements LibraryProvider{

    private final SteamClient steamClient;
    private final SteamService steamService;
    private final SteamGameMapper steamGameMapper;

    public SteamLibraryProvider() {
        this.steamClient = new SteamClient();
        this.steamService = new SteamService();
        this.steamGameMapper = new SteamGameMapper();
    }

    @Override
    public List<GameLibraryEntry> getOwnedGames() {
        List<GameLibraryEntry> gameLibraryEntries = new ArrayList<>();

        var parsedOwnedGames = steamService.parseOwnedGames(steamClient.getAllGames());

        for(GameStats game : parsedOwnedGames) {
            var playerAchievements = steamClient.getPlayerAchievements(game.getAppId());
            var achievementStats = steamService.getStats(playerAchievements);

            if(achievementStats == null) continue;

            gameLibraryEntries.add(
                    steamGameMapper.toGameLibrary(
                            game,
                            achievementStats
                    ));
        }
        return gameLibraryEntries;
    }

    @Override
    public List<GameLibraryEntry> getRecentGames() {
        List<GameLibraryEntry> recentGamesEntries = new ArrayList<>();

        var parsedRecentGames = steamService.parseRecentGames(steamClient.getRecentPlayedGames());

        for(GameStats game : parsedRecentGames) {
            String achievementJson = steamClient.getPlayerAchievements(game.getAppId());
            AchievementStats achievementStats = steamService.getStats(achievementJson);

            if (achievementJson == null) continue;

            if(game.getPlayTime2Weeks() > 0) {
                recentGamesEntries.add(steamGameMapper.toGameLibrary(game, achievementStats));
            }

        }
        return recentGamesEntries;
    }
}
