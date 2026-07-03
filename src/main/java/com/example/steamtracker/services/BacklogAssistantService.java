package com.example.steamtracker.services;

import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.providers.LibraryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BacklogAssistantService {

    private final LibraryProvider libraryProvider;

    public List<GameLibraryEntry> findBacklogRecommendation() {
        return libraryProvider.getOwnedGames()
                .stream()
                .filter(game ->
                        game.getGameStatus() == GameStatus.BACKLOG)
                .sorted(Comparator.comparingInt
                        (
                                GameLibraryEntry::getPlaytimeForever
                        )
                )
                .toList();
    }
}
