package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.clients.SteamClient;
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

    public void syncOwnedGames() {
        String ownedGamesJson = steamClient.getAllGames();

        List<GameStats> ownedGamesStats = steamService.parseOwnedGames(ownedGamesJson);

        List<List<Object>> ownedGamesList = new ArrayList<>();

        for(GameStats game : ownedGamesStats){
            String achievementJson = steamClient.getPlayerAchievements(game.getAppId());
            AchievementStats stats = steamService.getStats(achievementJson);

            int unlocked = stats.getUnlocked();
            int total = stats.getTotal();

            double percent = total > 0 ? (double) unlocked/total * 100 : 0;

            ownedGamesList.add(List.of(
                    game.getAppId(),
                    game.getName(),
                    game.getPlayTimeForever() + "h",
                    unlocked,
                    total,
                    String.format("%.0f%%", percent)
            ));
        }

        sheetsClient.writeLocal(
                SPREADSHEET_ID,
                "All_games!A2",
                ownedGamesList
        );
    }
}
