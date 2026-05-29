package com.example.steamtracker.providers.steam;

import com.example.steamtracker.clients.SteamClient;
import com.example.steamtracker.entities.AchievementProgress;
import com.example.steamtracker.providers.AchievementProvider;
import com.example.steamtracker.services.Steam.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SteamAchievementProvider implements AchievementProvider {

    @Autowired
    private SteamService steamService;
    @Autowired
    private SteamClient steamClient;

    @Override
    public AchievementProgress getAchievements(int externalId) {
        var playerAchievements = steamClient.getPlayerAchievements(externalId);

        return steamService.parseAchievementProgress(playerAchievements);
    }
}
