package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.clients.StoreClient;
import com.example.steamtracker.entities.GamePriceOffer;
import com.example.steamtracker.entities.WishListEntry;
import com.example.steamtracker.enums.GameStatus;
import com.example.steamtracker.providers.WishlistProvider;
import com.example.steamtracker.providers.steam.SteamPriceProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final SheetsClient sheetsClient;
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");
    private final SteamPriceProvider steamPriceProvider;
    private final WishlistProvider wishlistProvider;
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    public void syncWishlist() {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-003] Starting wishlist synchronization");

            Map<Integer, Integer> rowMap = getWishlistRowMap();

            List<Integer> sheetWishlist = getWishlistFromSheet();

            List<WishListEntry> currentWishlist = wishlistProvider.getWishlist();

            for(WishListEntry game : currentWishlist) {
                int appId = game.getGame().getExternalID();

                GamePriceOffer offer = steamPriceProvider.getPrice(appId);

                if (offer == null) continue;

                if (!sheetWishlist.contains(appId)) {

                    logger.info("New game found: {}", appId);
                    processNewGame(appId, offer);

                } else {
                    logger.info("Updating game: {} Name: {}",appId, offer.getGameName());
                    updateWishlistGame(
                            rowMap.get(appId),
                            appId,
                            offer
                    );
                }
            }

            List<Integer> currentSteamIds = getCurrentWishlistAppIds(currentWishlist);

            for (Integer sheetAppId : sheetWishlist) {
                if(!currentSteamIds.contains(sheetAppId)) {

                    removeGameFromWishlist(
                            rowMap.get(sheetAppId)
                            ,sheetAppId
                    );
                }
            }
            long duration = System.currentTimeMillis() - start;

            logger.info("[SYNC-003] Wishlist synchronization completed in {} ms"
                    ,duration);

        }catch (Exception e) {
            logger.error("[SYNC-003] Failed to synchronize wishlist", e);
        }

    }

    public List<Integer> getWishlistFromSheet(){
        try{
            var response = sheetsClient.getValues(
                    SPREADSHEET_ID,
                    "Test_WishList!A2:A"
            );

            List<Integer> games = new ArrayList<>();

            if (response == null || response.getValues() == null) {
                return new ArrayList<>(); // return empty list instead of null
            }

            for (List<Object> row : response.getValues()){

                if (row.isEmpty()) continue;

                games.add(Integer.parseInt(row.get(0).toString()));
            }

            return games;

        } catch (Exception e){
            logger.error("[SHEET-007] Failed to get wishlist from sheet",e);
            return new ArrayList<>();
        }
    }

    public void processNewGame(int appId, GamePriceOffer offer) {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-004] Processing new game in wishlist");

            if (offer == null) return;

            List<List<Object>> values = List.of(
                    List.of(
                            appId,
                            offer.getGameName(),
                            offer.getFinalPrice(),
                            String.format("%.0f%%", offer.getDiscount()),
                            GameStatus.WISHLIST.toString()
                            )
            );

            Thread.sleep(1000); // limit time for Sheets Quota of 60 writes per minute

            sheetsClient.appendToSheet(
                    SPREADSHEET_ID,
                    "Test_WishList!A2:A",
                    values);

            long duration = System.currentTimeMillis() - start;

            logger.info("[SYNC-004] New game processed completed in {} ms"
                    ,duration);

        }catch (Exception e) {
            logger.error("[SYNC-004] Failed to process new wishlist game", e);
        }
    }

    public Map<Integer, Integer> getWishlistRowMap(){
        Map<Integer, Integer> rows = new HashMap<>();

        var response = sheetsClient.getValues(
                SPREADSHEET_ID,
                "Test_WishList!A2:A"
        );

        if (response == null || response.getValues() == null) {
            logger.warn("Wishlist sheet is empty");
            return rows;
        }

        int rowIndex = 2;

        for(List<Object> row : response.getValues()){
            if(!row.isEmpty()) {
                int appId = Integer.parseInt(row.get(0).toString());

                rows.put(appId, rowIndex);
            }

            rowIndex++;
        }

        return rows;
    }

    public void updateWishlistGame(int row, int appId, GamePriceOffer offer) {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-005] Starting update wishlist");

            if (row == 0) {
                logger.error("Update Failed row is null... Check findRowByAppId response...");
                return;
            }

            List<List<Object>> values = List.of(
                    List.of(
                            appId,
                            offer.getGameName(),
                            offer.getFinalPrice(),
                            String.format("%.0f%%", offer.getDiscount()),
                            GameStatus.WISHLIST.toString()
                    ));

            Thread.sleep(1000);

            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Test_WishList!A" + row + ":E" + row,
                    values
            );

            long duration = System.currentTimeMillis() - start;
            logger.info("[SYNC-005] Updating wishlist completed in {} ms"
                    , duration);
        }catch (Exception e) {
            logger.error("[SYNC-005] Failed to update Wishlist game", e);
        }
    }

    public void removeGameFromWishlist(int row, int appId) {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-006] Wishlist game removal starting");

            if (row == 0) return;

            sheetsClient.clearRow(
                    SPREADSHEET_ID,
                    "Test_WishList!A" + row + ":E" + row);

            logger.info("Removed Game: {}", appId);

            long duration = System.currentTimeMillis() - start;

            logger.info("[SYNC-006] Wishlist game removal completed in {} ms"
                    , duration);

        }catch (Exception e) {
            logger.error("[SYNC-006] Failed to remove wishlist game", e);
        }
    }

    public List<Integer> getCurrentWishlistAppIds(List<WishListEntry> wishList){
        try{
            List<Integer> ids = new ArrayList<>();

            for (WishListEntry game : wishList) {
                ids.add(game.getGame().getExternalID());
            }

            return ids;
        }catch (Exception e) {
            logger.error("[SHEET-009] Failed to get Wishlist app IDs from sheet", e);
            return new ArrayList<>();
        }
    }
}
