package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.clients.StoreClient;
import com.example.steamtracker.models.GamePrice;
import com.example.steamtracker.models.GameSearchResult;
import com.example.steamtracker.models.WishlistModel;
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

    public void syncWishlist() {
        System.out.println("Running Wishlist Sync...");

        String json = storeClient.getWishlist();

        List<WishlistModel> currentWishlist = steamService.parseWishlist(json);

        List<Integer> sheetWishlist = getWishlistFromSheet();

        for(WishlistModel game : currentWishlist) {
            int appId = game.getAppId();

            String gameDetails = storeClient.getGameDetails(appId);

            GamePrice price = storeService.parsePrice(gameDetails, appId);

            if (price == null) continue;

            if (!sheetWishlist.contains(appId)) {

                System.out.println("New game found: " + appId);

                processNewGame(appId);
            } else {
                System.out.println("Updating game " + appId + " Name: " + price.getName());
                updateWishlistGame(appId, price);
            }
        }

        List<Integer> currentSteamIds = getCurrentWishlistAppIds(currentWishlist);

        for (Integer sheetAppId : sheetWishlist) {
            if(!currentSteamIds.contains(sheetAppId)) {

                removeGameFromWishlist(sheetAppId);
            }
        }

        System.out.println("Syncing finalized...");
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void processNewGame(int gameId) {
        try{

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

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer findRowByAppId(int appId){
        try {
            var response = sheetsClient.getValues(SPREADSHEET_ID, "Test_WishList!A2:A");

            if(response == null || response.getValues() == null) {
                System.err.println("Sheet is empty...");
                return null;
            }

            int rowIndex = 2;

            for(List<Object> row : response.getValues()){
                if(!row.isEmpty()) {
                    int sheetAppId = Integer.parseInt(row.get(0).toString());

                    if(sheetAppId == 0 || sheetAppId < 0){
                        System.err.println("App ID is invalid or empty");
                        return null;
                    }

                    if(sheetAppId == appId) {
                        return rowIndex;
                    }
                }

                rowIndex++;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateWishlistGame(int appId, GamePrice price) {
        Integer row = findRowByAppId(appId);

        if(row == null) {
            System.err.println("Update Failed row is null... Check findRowByAppId response...");
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
    }

    public void removeGameFromWishlist(int appId) {
        Integer row = findRowByAppId(appId);

        if (row == null) return;

        sheetsClient.clearRow(
                SPREADSHEET_ID,
                "Test_WishList!A" + row + ":D" + row);

        System.out.println("Removed Game: " + appId);
    }

    public List<Integer> getCurrentWishlistAppIds(List<WishlistModel> wishList){
        List<Integer> ids = new ArrayList<>();

        for(WishlistModel game : wishList) {
            ids.add(game.getAppId());
        }
        return ids;
    }
}
