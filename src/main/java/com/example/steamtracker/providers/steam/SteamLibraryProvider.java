package com.example.steamtracker.providers.steam;

import com.example.steamtracker.clients.SteamClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.mappers.SteamGameMapper;
import com.example.steamtracker.models.GameStats;
import com.example.steamtracker.providers.AchievementProvider;
import com.example.steamtracker.providers.LibraryProvider;
import com.example.steamtracker.services.CompletionTierService;
import com.example.steamtracker.services.GameStatusOverrideService;
import com.example.steamtracker.services.GameStatusService;
import com.example.steamtracker.services.Steam.SteamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SteamLibraryProvider implements LibraryProvider {

    private final SteamClient steamClient;
    private final SteamService steamService;
    private final SteamGameMapper steamGameMapper;
    private final AchievementProvider achievementProvider;
    private final GameStatusService gameStatusService;
    private final CompletionTierService completionTierService;
    private final GameStatusOverrideService overrideService;


    @Override
    public List<GameLibraryEntry> getOwnedGames() {
        List<GameLibraryEntry> gameLibraryEntries = new ArrayList<>();

        var parsedOwnedGames = steamService.parseOwnedGames(steamClient.getAllGames());

        Map<Integer, GameStatus> overrides =
                overrideService.getOverrideMap();

        for(GameStats gameStats : parsedOwnedGames) {
            var progress = achievementProvider.getAchievements(gameStats.getAppId());

            GameStatus override =
                    overrides.get(
                            gameStats.getAppId()
                    );

            if(progress == null) continue;

            var completionTier = completionTierService.determineCompletion(progress);

            GameStatus status;

            if(override != null) {
                status = override;
            } else {
                status = gameStatusService.determineStatus(
                        gameStats,
                        progress,
                        completionTier
                );
            }


            gameLibraryEntries.add(
                    steamGameMapper.toGameLibrary(
                            gameStats,
                            progress,
                            status,
                            completionTier
                    ));
        }
        return gameLibraryEntries;
    }

    @Override
    public List<GameLibraryEntry> getRecentGames() {
        List<GameLibraryEntry> recentGamesEntries = new ArrayList<>();

        List<GameStats> parsedRecentGames = steamService.parseRecentGames(steamClient.getRecentPlayedGames());

        for(GameStats game : parsedRecentGames) {
            var progress = achievementProvider.getAchievements(game.getAppId());

            var completionTier = completionTierService.determineCompletion(progress);

            var status = gameStatusService.determineStatus(
                    game,
                    progress,
                    completionTier
            );

            if (progress == null) continue;

            if(game.getPlayTime2Weeks() > 0) {
                recentGamesEntries.add(
                        steamGameMapper.toGameLibrary(
                                game,
                                progress,
                                status,
                                completionTier
                        ));
            }

        }
        return recentGamesEntries;
    }
}
