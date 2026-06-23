package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.models.GamingStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamingStatsSheetService {

    private final GamingStatsService gamingStatsService;
    private final SheetsClient sheetsClient;
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    public void syncGamingStats() {
        GamingStats stats = gamingStatsService.generateStats();

        List<List<Object>> values = List.of(
                List.of("Total Owned Games", stats.getTotalOwnedGames()),
                List.of("Backlog Games", stats.getBacklogGames()),
                List.of("Playing Games", stats.getPlayingGames()),
                List.of("Completed Games", stats.getCompletedGames()),
                List.of("Abandoned Games", stats.getAbandonedGames()),
                List.of("Completion Rate", String.format("%.1f%%",
                        stats.getCompletionRate())),
                List.of("Perfected Games", stats.getPerfectedGames()),
                List.of("Mastered Games", stats.getMasteredGames()),
                List.of("Story Cleared Games", stats.getStoryClearedGames()),
                List.of("In Progress Games", stats.getInProgressGames()),
                List.of("Unstarted Games", stats.getUnstartedGames())
        );

        sheetsClient.clearRange(
                SPREADSHEET_ID,
                "Gaming_Stats!A2"
        );

        sheetsClient.writeLocal(
                SPREADSHEET_ID,
                "Gaming_Stats!A2",
                values
        );
    }
}
