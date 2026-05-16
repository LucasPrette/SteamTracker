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

    @Scheduled(fixedRate = 30000)
    public void syncWishlistJob() {
        wishlistService.syncWishlist();
    }

//    @Scheduled(fixedRate = 3600000)
//    public void syncOwnedGamesJob() {
//        ownedGamesService.syncOwnedGames();
//    }
//
//    @Scheduled(fixedRate = 3600000)
//    public void syncRecentGamesService() {
//        recentGamesService.syncRecentGames();
//    }



}
