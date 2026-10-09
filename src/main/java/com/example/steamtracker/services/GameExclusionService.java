package com.example.steamtracker.services;


import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.entities.GameLibraryEntry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameExclusionService {

    private final SheetsClient sheetsClient;
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    private static final Logger logger = LoggerFactory.getLogger(GameExclusionService.class);


    public Set<Integer> getExcludedAppIds() {
        try {
            var response = sheetsClient.getValues(
                    SPREADSHEET_ID,
                    "Game_Exclusions!A2:A"
            );

            Set<Integer> excludedAppIds = new HashSet<>();

            if (response == null || response.getValues() == null) {
                return excludedAppIds;
            }

            for (List<Object> row : response.getValues()) {
                if (row.isEmpty() || row.get(0) == null) {
                    continue;
                }

                String value = row.get(0).toString().trim();

                if (value.isEmpty()) {
                    continue;
                }

                try {
                    excludedAppIds.add(new BigDecimal(value).intValueExact());
                } catch (NumberFormatException | ArithmeticException e) {
                    logger.warn("Skipping invalid APP ID in Game_exclusions: {}", value);
                }
            }
            return excludedAppIds;
        } catch (Exception e) {
            logger.error("Failed to load excluded App ids", e);
            throw new IllegalStateException("Could not load game exclusions", e);
        }
    }

    public List<GameLibraryEntry> filterIncludedGames(List<GameLibraryEntry> games) {
        Set<Integer> excludedAppIds = getExcludedAppIds();

        return games.stream()
                .filter(game ->
                        !excludedAppIds.contains(
                                game.getGame().getExternalID()
                        )
                )
                .toList();
    }
}
