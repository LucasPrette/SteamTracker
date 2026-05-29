package com.example.steamtracker.providers;

import com.example.steamtracker.entities.GameLibraryEntry;

import java.util.List;

public interface LibraryProvider {
    List<GameLibraryEntry> getOwnedGames();
    List<GameLibraryEntry> getRecentGames();
}
