package com.example.steamtracker.services.sheet;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.services.ResumeAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeAssistantSheetService {
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    private final SheetsClient sheetsClient;
    private final ResumeAssistantService resumeAssistantService;

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
                                game.getGameStatus().toString()
                        ))
                .toList();

        sheetsClient.clearRange(
                SPREADSHEET_ID,
                "Resume_Assistant!A2:F"
        );

        if(!values.isEmpty()) {
            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Resume_Assistant!A2",
                    values
            );
        }
    }
}
