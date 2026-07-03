package com.example.steamtracker.services;

import com.example.steamtracker.entities.GameLibraryEntry;
import com.example.steamtracker.models.GamingAdvice;
import com.example.steamtracker.providers.LibraryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GamingAdvisorService {

    private final LibraryProvider libraryProvider;
    private final NearCompletionService nearCompletionService;
    private final ResumeAssistantService resumeAssistantService;
    private final BacklogAssistantService backlogAssistantService;

    public List<GamingAdvice> generateAdvice() {
        List<GameLibraryEntry> games = libraryProvider.getOwnedGames();

        List<GamingAdvice> advice = new ArrayList<>();

        nearCompletionService.findNearCompletionGames(games)
                .stream()
                .findFirst()
                .ifPresent(game -> advice.add(
                        new GamingAdvice(
                                "Finish Next",
                                game.getGame().getGameName(),
                                String.format(
                                        "%.1f%%",
                                        getProgress(game)
                                )
                        )
                ));

        resumeAssistantService.findGamesToResume(games)
                .stream()
                .findFirst()
                .ifPresent(game -> advice.add(
                        new GamingAdvice(
                                "Resume Next",
                                game.getGame().getGameName(),
                                String.format(
                                        "%.1f%% Complete, Status: %s",
                                        getProgress(game),
                                        game.getGameStatus()
                                )
                        )
                ));

        backlogAssistantService.findBacklogRecommendation(games)
                .stream()
                .findFirst()
                .ifPresent(game -> advice.add(
                        new GamingAdvice(
                                "Start Next",
                                game.getGame().getGameName(),
                                "Never played"
                        )
                ));

        return advice;
    }

    private double getProgress(GameLibraryEntry game) {
        if(game.getAchievements() == null) {
            return 0;
        }

        return game.getAchievements()
                .getCompletionPercentage();
    }
}
