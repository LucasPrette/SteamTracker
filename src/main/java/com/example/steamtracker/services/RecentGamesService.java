package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.providers.LibraryProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentGamesService {
    private final LibraryProvider libraryProvider;
    private final SheetsClient sheetsClient;
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    private static final Logger logger = LoggerFactory.getLogger(RecentGamesService.class);

    public void syncRecentGames () {

        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-002] Starting recent games synchronization");

            List<GameLibraryEntry> games =
                    libraryProvider.getRecentGames();

            List<List<Object>> playingValues = games.stream()
                            .map(gameLibraryEntry -> List.<Object>of(
                                    gameLibraryEntry.getGame().getExternalID(),
                                    gameLibraryEntry.getGame().getGameName(),
                                    gameLibraryEntry.getRecentPlaytime(),
                                    gameLibraryEntry.getAchievements().getUnlocked(),
                                    gameLibraryEntry.getAchievements().getTotal(),
                                    String.format(
                                            "%.0f%%",
                                            gameLibraryEntry.getAchievements().getCompletionPercentage()
                                    ),
                                    gameLibraryEntry.getGameStatus().toString()
                            ))
                    .toList();

            sheetsClient.clearRange(
                    SPREADSHEET_ID,
                    "Test_Playing!A2:G"
            );

            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Test_Playing!A2",
                    playingValues
            );

            long duration = System.currentTimeMillis() - start;

            logger.info("[SYNC-002] Recent games synchronization completed in {} ms",
                    duration);

        } catch (Exception e) {
            logger.error("[SYNC-002] Failed to synchronize Recent Games", e);
        }

    }
}
