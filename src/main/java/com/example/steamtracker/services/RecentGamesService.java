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
public class RecentGamesService {

    @Autowired
    private SteamClient steamClient;

    @Autowired
    private SteamService steamService;

    @Autowired
    private SheetsClient sheetsClient;

    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");


    public void syncRecentGames () {
        String json = steamClient.getRecentPlayedGames();

        List<GameStats> games = steamService.parseRecentGames(json);
        List<List<Object>> playingValues = new ArrayList<>();

        for (GameStats game : games) {
            String achievementJson = steamClient.getPlayerAchievements(game.getAppId());
            AchievementStats stats = steamService.getStats(achievementJson);

            int unlocked = stats.getUnlocked();
            int total = stats.getTotal();

            double percent = total > 0 ? (double) unlocked/total * 100 : 0;

            if(game.getPlayTime2Weeks() > 0) {
                playingValues.add(List.of(
                        game.getAppId(),
                        game.getName(),
                        game.getPlayTime2Weeks(),
                        unlocked,
                        total,
                        String.format("%.0f%%", percent)
                ));
            }

        }

        sheetsClient.writeLocal(
                SPREADSHEET_ID,
                "Test_Playing!A2",
                playingValues
        );
    }
}
