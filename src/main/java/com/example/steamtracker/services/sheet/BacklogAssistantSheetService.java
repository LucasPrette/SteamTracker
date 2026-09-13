package com.example.steamtracker.services.sheet;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.services.BacklogAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BacklogAssistantSheetService {

    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    private final BacklogAssistantService backlogAssistantService;
    private final SheetsClient sheetsClient;

    public void syncBacklogAssistant() {
        List<GameLibraryEntry> games = backlogAssistantService.findBacklogRecommendation();

        List<List<Object>> values = games.stream()
                .map(game -> {
                    AchievementProgress achievements = game .getAchievements();

                    double progress = achievements == null
                            ? 0
                            : achievements.getCompletionPercentage();

                    String achievementCount = achievements == null
                            ? "0/0"
                            : achievements.getUnlocked()
                            + "/"
                    + achievements.getTotal();

                    return List.<Object>of(
                            game.getGame().getExternalID(),
                            game.getGame().getGameName(),
                            String.format(
                                    "%.1f",
                                    (double)game.getPlaytimeForever()
                            ),
                            String.format("%.1f%%", progress),
                            achievementCount,
                            game.getCompletionTier().toString()
                    );
                })
                .toList();

        sheetsClient.clearRange(
                SPREADSHEET_ID,
                "Backlog_Assistant!A2:F"
        );

        if(!values.isEmpty()) {
            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Backlog_Assistant!A2",
                    values
            );
        }
    }
}
