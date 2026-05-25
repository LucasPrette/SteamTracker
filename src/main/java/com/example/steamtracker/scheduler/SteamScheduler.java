package com.example.steamtracker.scheduler;

import com.example.steamtracker.services.OwnedGamesService;
import com.example.steamtracker.services.RecentGamesService;
import com.example.steamtracker.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SteamScheduler {
    private final WishlistService wishlistService;
    private final OwnedGamesService ownedGamesService;
    private final RecentGamesService recentGamesService;

    @Scheduled(cron = "0 0 12,0 * * *",
    zone = "America/Sao_Paulo")
    public void syncWishlistJob() {
        wishlistService.syncWishlist();
    }

    @Scheduled(cron = "0 * * * * *",
            zone = "America/Sao_Paulo")
    public void syncOwnedGamesJob() {
        ownedGamesService.syncOwnedGames();
    }

    @Scheduled(cron = "0 * * * * *",
            zone = "America/Sao_Paulo")
    public void syncRecentGamesService() {
        recentGamesService.syncRecentGames();
    }



}
