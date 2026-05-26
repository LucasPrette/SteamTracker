package com.example.steamtracker.scheduler;

import com.example.steamtracker.services.OwnedGamesService;
import com.example.steamtracker.services.RecentGamesService;
import com.example.steamtracker.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SteamScheduler {
    private final WishlistService wishlistService;
    private final OwnedGamesService ownedGamesService;
    private final RecentGamesService recentGamesService;

    private static final Logger logger = LoggerFactory.getLogger(SteamScheduler.class);

    @Scheduled(cron = "0 0 12,0 * * *",
    zone = "America/Sao_Paulo")
    public void syncWishlistJob() {
        try{
            logger.info("[SCHED-001] Triggering Wishlist scheduler");

            wishlistService.syncWishlist();

            logger.info("[SCHED-001] Wishlist scheduler execution finished");
        } catch (Exception e) {
            logger.error("[SCHED-001] Wishlist scheduler execution failed");
        }
    }

    @Scheduled(cron = "0 0 0 * * 7",
            zone = "America/Sao_Paulo")
    public void syncOwnedGamesJob() {
        try{
            logger.info("[SCHED-002] Triggering owned games scheduler");

            ownedGamesService.syncOwnedGames();

            logger.info("[SCHED-002] Owned games scheduler execution finished");
        }catch (Exception e) {
            logger.error("[SCHED-002] Owned Games scheduler execution failed");
        }
    }

    @Scheduled(cron = "0 0 18-23/2,0-3/2 * * *",
            zone = "America/Sao_Paulo")
    public void syncRecentGamesJob() {
        try{
            logger.info("[SCHED-003] Triggering recent games scheduler");

            recentGamesService.syncRecentGames();

            logger.info("[SCHED-003] Recent games scheduler execution finished");
        }catch (Exception e) {

            logger.error("[SCHED-003] Recent Games scheduler execution failed");
        }
    }



}
