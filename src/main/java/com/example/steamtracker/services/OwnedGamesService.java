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
public class OwnedGamesService {
    private final SheetsClient sheetsClient;
    private final LibraryProvider libraryProvider;
    private final GameStatusService gameStatusService;

    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");
    private static final Logger logger = LoggerFactory.getLogger(OwnedGamesService.class);

    public void syncOwnedGames() {
        try{
            long start = System.currentTimeMillis();

            logger.info("[SYNC-001] Starting owned games synchronization");

            List<GameLibraryEntry> games =
                    libraryProvider.getOwnedGames();

            List<
                    List<Object>> ownedGamesList =
                    games.stream()
                            .map(gameLibraryEntry -> List.<Object>of(
                                    gameLibraryEntry.getGame().getExternalID(),
                                    gameLibraryEntry.getGame().getGameName(),
                                    gameLibraryEntry.getPlaytimeForever(),
                                    gameLibraryEntry.getAchievements().getUnlocked(),
                                    gameLibraryEntry.getAchievements().getTotal(),
                                    String.format(
                                            "%.0f%%",
                                            gameLibraryEntry.getAchievements()
                                                    .getCompletionPercentage()
                                    ),
                                    gameLibraryEntry.getGameStatus().toString(),
                                    gameLibraryEntry.getCompletionTier().toString()
                            )
                    ).toList();

            sheetsClient.clearRange(
                    SPREADSHEET_ID,
                    "All_games!A2:H"
            );

            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "All_games!A2",
                    ownedGamesList
            );

            long duration = System.currentTimeMillis() - start;

            logger.info(
                    "[SYNC-001] Owned games synchronization completed in {} ms",
                    duration
            );

        } catch (Exception e) {
            logger.error("[SYNC-001] Failed to synchronize owned games", e);
        }

    }
}
