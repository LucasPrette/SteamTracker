package com.example.steamtracker.services;


import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.CompletionTier;
import com.example.steamtracker.providers.LibraryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NearCompletionService {

    private final LibraryProvider libraryProvider;

    public List<GameLibraryEntry> findNearCompletionGames() {
        return libraryProvider.getOwnedGames()
                .stream()
                .filter(game -> game.getAchievements() != null)
                .filter(game -> game.getAchievements().getTotal() > 0)
                .filter(game ->
                        game.getCompletionTier() == CompletionTier.MASTERED)
                .sorted(
                        Comparator.comparingDouble(
                                (GameLibraryEntry game) -> game.getAchievements()
                                        .getCompletionPercentage()
                        ).reversed()
                )
                .toList();
    }

    public List<GameLibraryEntry> findNearCompletionGames(
            List<GameLibraryEntry> games
    ) {
        return games
                .stream()
                .filter(game -> game.getAchievements() != null)
                .filter(game -> game.getAchievements().getTotal() > 0)
                .filter(game ->
                        game.getCompletionTier() == CompletionTier.MASTERED)
                .sorted(
                        Comparator.comparingDouble(
                                (GameLibraryEntry game) -> game.getAchievements()
                                        .getCompletionPercentage()
                        ).reversed()
                )
                .toList();
    }

}

