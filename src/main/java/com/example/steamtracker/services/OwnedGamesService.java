package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.clients.SteamClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.mappers.SteamGameMapper;
import com.example.steamtracker.models.AchievementStats;
import com.example.steamtracker.models.GameStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnedGamesService {
    @Autowired
    private SteamClient steamClient;
    @Autowired
    private SteamService steamService;
    @Autowired
    private SheetsClient sheetsClient;
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");
    @Autowired
    private SteamGameMapper steamGameMapper;

    public void syncOwnedGames() {
        String ownedGamesJson = steamClient.getAllGames();

        List<GameStats> ownedGamesStats = steamService.parseOwnedGames(ownedGamesJson);

        List<List<Object>> ownedGamesList = new ArrayList<>();

        for(GameStats game : ownedGamesStats){
            String achievementJson = steamClient.getPlayerAchievements(game.getAppId());
            AchievementStats stats = steamService.getStats(achievementJson);

            GameLibraryEntry entry = steamGameMapper.toGameLibrary(game, stats);

            ownedGamesList.add(
                    List.of(
                            entry.getGame().getExternalID(),
                            entry.getGame().getGameName(),
                            entry.getPlaytime(),
                            entry.getAchievements().getUnlocked(),
                            entry.getAchievements().getTotal(),
                            String.format(
                                    "%.0f%%",
                                    entry.getAchievements()
                                            .getCompletionPercentage()
                            )
            ));
        }

        sheetsClient.writeLocal(
                SPREADSHEET_ID,
                "All_games!A2",
                ownedGamesList
        );
    }
}
