package com.example.steamtracker.services;

import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.CompletionTier;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.models.GamingStats;
import com.example.steamtracker.providers.LibraryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamingStatsService {

    private final LibraryProvider libraryProvider;

    public GamingStats generateStats() {
        List<GameLibraryEntry> games = libraryProvider.getOwnedGames();

        GamingStats stats = new GamingStats();

        stats.setPerfectedGames(
                (int) games.stream()
                        .filter(game ->
                                game.getCompletionTier() == CompletionTier.PERFECTED)
                        .count()
        );

        stats.setMasteredGames(
                (int) games.stream()
                        .filter(game ->
                                game.getCompletionTier() == CompletionTier.MASTERED)
                        .count()
        );

        stats.setStoryClearedGames(
                (int) games.stream()
                        .filter(game ->
                                game.getCompletionTier() == CompletionTier.STORY_CLEARED)
                        .count()
        );

        stats.setInProgressGames(
                (int) games.stream()
                        .filter(game ->
                                game.getCompletionTier() == CompletionTier.IN_PROGRESS)
                        .count()
        );

        stats.setUnstartedGames(
                (int) games.stream()
                        .filter(game ->
                                game.getCompletionTier() == CompletionTier.UNSTARTED)
                        .count()
        );

        stats.setAbandonedGames(
                (int) games.stream()
                        .filter(game ->
                                game.getGameStatus() == GameStatus.ABANDONED)
                        .count()
        );

        stats.setBacklogGames(
                (int) games.stream()
                        .filter(game ->
                                game.getGameStatus() == GameStatus.BACKLOG)
                        .count()
        );

        stats.setCompletedGames(
                (int) games.stream()
                        .filter(game ->
                                game.getGameStatus() == GameStatus.COMPLETED)
                        .count()
        );

        stats.setPlayingGames(
                (int) games.stream()
                        .filter(game ->
                                game.getGameStatus() == GameStatus.PLAYING)
                        .count()
        );

        stats.setTotalOwnedGames(games.size());

        if(stats.getTotalOwnedGames() > 0) {
            stats.setCompletionRate(
                    (double) stats.getCompletedGames()
                            / stats.getTotalOwnedGames()
                            * 100
            );
        }

        return stats;
    }
}
