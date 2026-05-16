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
            if(!sheetWishlist.contains(game.getAppId())){
                System.out.println("New game found: " + game);

                processNewGame(game.getAppId());
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
                            "R$ " + price.getFinalPrice(),
                            price.getDiscount() + "%"
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
}
