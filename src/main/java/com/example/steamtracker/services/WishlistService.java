package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.clients.StoreClient;
import com.example.steamtracker.models.GamePrice;
import com.example.steamtracker.models.GameSearchResult;
import com.example.steamtracker.models.WishlistModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {
    @Autowired
    private StoreClient storeClient;
    @Autowired
    private SheetsClient sheetsClient;
    @Autowired
    private SteamService steamService;

    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");
    @Autowired
    private StoreService storeService;

    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    public void syncWishlist() {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-003] Starting wishlist synchronization");

            String json = storeClient.getWishlist();

            List<WishlistModel> currentWishlist = steamService.parseWishlist(json);

            List<Integer> sheetWishlist = getWishlistFromSheet();

            for(WishlistModel game : currentWishlist) {
                int appId = game.getAppId();

                String gameDetails = storeClient.getGameDetails(appId);

                GamePrice price = storeService.parsePrice(gameDetails, appId);

                if (price == null) continue;

                if (!sheetWishlist.contains(appId)) {

                    logger.info("New game found: {}", appId);

                    processNewGame(appId);
                } else {
                    logger.info("Updating game: {} Name: {}",appId, price.getName());
                    updateWishlistGame(appId, price);
                }
            }

            List<Integer> currentSteamIds = getCurrentWishlistAppIds(currentWishlist);

            for (Integer sheetAppId : sheetWishlist) {
                if(!currentSteamIds.contains(sheetAppId)) {

                    removeGameFromWishlist(sheetAppId);
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

    public void processNewGame(int gameId) {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-004] Processing new game in wishlist");

            String searchJson = storeClient.searchGame(gameId);

            GameSearchResult result = steamService.resolveAppId(searchJson);

            if (result == null){
                return;
            }

            int appId = result.getAppId();

            String detailsJson = storeClient.getGameDetails(appId);

            GamePrice price = storeService.parsePrice(detailsJson, appId);

            if (price == null) return;

            List<List<Object>> values = List.of(
                    List.of(
                            appId,
                            price.getName(),
                            price.getFinalPrice(),
                            String.format("%.0f%%", price.getDiscount())
                            )
            );

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

    public Integer findRowByAppId(int appId){
        try {
            var response = sheetsClient.getValues(SPREADSHEET_ID, "Test_WishList!A2:A");

            if(response == null || response.getValues() == null) {
                logger.error("Sheet is empty...");
                return null;
            }

            int rowIndex = 2;

            for(List<Object> row : response.getValues()){
                if(!row.isEmpty()) {
                    int sheetAppId = Integer.parseInt(row.get(0).toString());

                    if(sheetAppId == 0 || sheetAppId < 0){
                        logger.error("App ID is invalid or empty");
                        return null;
                    }

                    if(sheetAppId == appId) {
                        return rowIndex;
                    }
                }

                rowIndex++;
            }

        }catch (Exception e) {
            logger.error("[SHEET-008] Failed to find App ID on row", e);
        }
        return null;
    }

    public void updateWishlistGame(int appId, GamePrice price) {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-005] Starting update wishlist");
            Integer row = findRowByAppId(appId);

            if (row == null) {
                logger.error("Update Failed row is null... Check findRowByAppId response...");
                return;
            }

            List<List<Object>> values = List.of(
                    List.of(
                            appId,
                            price.getName(),
                            price.getFinalPrice(),
                            String.format("%.0f%%", price.getDiscount())
                    ));

            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Test_WishList!A" + row + ":D" + row,
                    values
            );

            long duration = System.currentTimeMillis() - start;
            logger.info("[SYNC-005] Updating wishlist completed in {} ms"
                    , duration);
        }catch (Exception e) {
            logger.error("[SYNC-005] Failed to update Wishlist game", e);
        }
    }

    public void removeGameFromWishlist(int appId) {
        try{
            long start = System.currentTimeMillis();
            logger.info("[SYNC-006] Wishlist game removal starting");
            Integer row = findRowByAppId(appId);

            if (row == null) return;

            sheetsClient.clearRow(
                    SPREADSHEET_ID,
                    "Test_WishList!A" + row + ":D" + row);

            logger.info("Removed Game: {}", appId);
            long duration = System.currentTimeMillis() - start;
            logger.info("[SYNC-006] Wishlist game removal completed in {} ms"
                    , duration);
        }catch (Exception e) {
            logger.error("[SYNC-006] Failed to remove wishlist game", e);
        }
    }

    public List<Integer> getCurrentWishlistAppIds(List<WishlistModel> wishList){
        try{
            List<Integer> ids = new ArrayList<>();

            for (WishlistModel game : wishList) {
                ids.add(game.getAppId());
            }
            return ids;
        }catch (Exception e) {
            logger.error("[SHEET-009] Failed to get Wishlist app IDs from sheet", e);
            return new ArrayList<>();
        }
    }
}
