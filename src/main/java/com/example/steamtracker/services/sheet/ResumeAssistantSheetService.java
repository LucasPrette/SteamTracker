package com.example.steamtracker.services.sheet;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.scheduler.SteamScheduler;
import com.example.steamtracker.services.ResumeAssistantService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeAssistantSheetService {
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    private final SheetsClient sheetsClient;
    private final ResumeAssistantService resumeAssistantService;
    private static final Logger logger = LoggerFactory.getLogger(SteamScheduler.class);

    public void syncResumeAssistant() {
        List<GameLibraryEntry> games = resumeAssistantService.findGamesToResume();

        List<List<Object>> values = games.stream()
                .map(game ->
                        List.<Object>of(
                                game.getGame().getExternalID(),
                                game.getGame().getGameName(),
                                String.format(
                                        "%.1f%%",
                                        game.getAchievements().getCompletionPercentage()
                                ),
                                game.getAchievements().getUnlocked() + "/" + game.getAchievements().getTotal(),
                                String.format("%.1f",
                                        (double)game.getPlaytimeForever()),
                                instantToDays(game.getLastPlayed()) + " Days",
                                game.getGameStatus().toString()
                        ))
                .toList();

        sheetsClient.clearRange(
                SPREADSHEET_ID,
                "Resume_Assistant!A2:G"
        );

        if(!values.isEmpty()) {
            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Resume_Assistant!A2",
                    values
            );
        }
    }

    private long instantToDays (long lastPlayed) {

        try {
            Instant currentDate = Instant.now();

            Instant lastPlayedDate = Instant.ofEpochSecond(lastPlayed);

            return ChronoUnit.DAYS.between(lastPlayedDate, currentDate);
        }catch (Exception e) {
            logger.error("[PARSE - 004] Failed Parsing days ");
        }

        return 0;
    }
}
