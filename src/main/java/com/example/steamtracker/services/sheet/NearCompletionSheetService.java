package com.example.steamtracker.services.sheet;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.services.NearCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NearCompletionSheetService {
    private final NearCompletionService nearCompletionService;
    private final SheetsClient sheetsClient;

    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    public void syncNearCompletion(){
        List<GameLibraryEntry> games =
                nearCompletionService.findNearCompletionGames();

        List<List<Object>> values = games.stream()
                .map(game -> List.<Object>of(
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
                "Near_Completion!A2:F"
        );

        if(!values.isEmpty()) {
            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Near_Completion!A2",
                    values
            );
        }
    }
}
