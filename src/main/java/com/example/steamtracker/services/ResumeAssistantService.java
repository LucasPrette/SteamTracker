package com.example.steamtracker.services;

import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.CompletionTier;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.providers.LibraryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ResumeAssistantService {

    private final LibraryProvider libraryProvider;


    public List<GameLibraryEntry> findGamesToResume() {
        return libraryProvider.getOwnedGames()
                .stream()
                .filter(game ->
                        game.getCompletionTier()
                        == CompletionTier.IN_PROGRESS
                )
                .filter(game ->
                        game.getGameStatus() == GameStatus.ABANDONED
                        || game.getGameStatus() == GameStatus.BACKLOG)
                .filter(game -> game.getAchievements() != null)
                .filter(game -> game.getAchievements().getTotal() > 0)
                .sorted(
                        Comparator.comparingDouble(
                                (GameLibraryEntry game) ->
                                        game.getAchievements().getCompletionPercentage()
                        ).reversed()
                )
                .toList();
    }
}
