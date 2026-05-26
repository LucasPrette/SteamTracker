package com.example.steamtracker.services;

import com.example.steamtracker.models.AchievementStats;
import com.example.steamtracker.models.GameSearchResult;
import com.example.steamtracker.models.GameStats;
import com.example.steamtracker.models.WishlistModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SteamService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String API_URL = "https://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/";

    private final String API_KEY = System.getenv("STEAM_API_KEY");
    private final String STEAM_ID = System.getenv("STEAM_ID");

    private static final Logger logger = LoggerFactory.getLogger(SteamService.class);

    public AchievementStats getAchievementProgress(int appId){
        try{
            String url = API_URL
                    + "?appid=" + appId
                    + "&key=" + API_KEY +
                    "&steamid=" + STEAM_ID;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)).GET().build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());

            JsonNode achievements = root.path("playerstats").path("achievements");

            int total = achievements.size();
            int unlocked = 0;

            for (JsonNode a : achievements) {
                if (a.get("achieved").asInt() == 1) {
                    unlocked++;
                }
            }

            return new AchievementStats(unlocked, total);


        } catch (Exception e) {
            logger.error("[STEAM-004] Failed to get achievement progress", e);
            return null;
        }
    }

    public AchievementStats getStats(String json) {

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(json);

            com.fasterxml.jackson.databind.JsonNode achievements = root
                    .path("playerstats")
                    .path("achievements");

            int unlocked = 0;

            for (com.fasterxml.jackson.databind.JsonNode achievement : achievements) {
                if (achievement.path("achieved").asInt() == 1) {
                    unlocked++;
                }
            }

            return new AchievementStats(unlocked, achievements.size());

        } catch (Exception e) {
            logger.error("[STEAM-005] Faile to get game stats", e);
            return null;
        }
    }

    public List<GameStats> parseRecentGames(String json){
        List<GameStats> gameList = new ArrayList<>();

        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode games = root.path("response").path("games");

            for(JsonNode game : games){
                int appId = game.path("appid").asInt();
                String gameName = game.path("name").asString();
                int playTimeForever = game.path("playtime_forever").asInt() / 60;
                int playTime2Weeks = game.path("playtime_2weeks").asInt() / 60;

                GameStats gameStats = new GameStats(appId, gameName, playTimeForever, playTime2Weeks);
                gameList.add(gameStats);
            }

        }catch (Exception e){
            logger.error("[PARSE-001] Failed to parse recent games",e);
            return null;
        }

        return gameList;
    }

    public List<GameStats> parseOwnedGames(String json){
        List<GameStats> ownedGamesList = new ArrayList<>();

        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode games = root.path("response").path("games");

            for(JsonNode game : games) {
                int appId = game.path("appid").asInt();
                String gameName = game.path("name").asString();
                int playtimeTotal = game.path("playtime_forever").asInt() / 60;

                ownedGamesList.add(new GameStats(appId,gameName,playtimeTotal, 0));
            }
        }catch (Exception e){
            logger.error("[PARSE-002] Failed to parse Owned Games", e);
            return null;
        }
        return ownedGamesList;
    }

    public GameSearchResult resolveAppId(String json) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode items = root.path("items");

            if(items.isEmpty()) return null;

            JsonNode first = items.get(0);

            int appId = first.path("id").asInt();
            String name = first.path("name").asString();

            return new GameSearchResult(appId, name);

        }catch (Exception e) {
            logger.error("[RESOLVE-001] Failed to resolve App ID",e);
            return null;
        }
    }

    public List<WishlistModel> parseWishlist(String json) {
        List<WishlistModel> wishlistList = new ArrayList<>();
        String dateStr = "2024-12-31";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode games = root.path("response").path("items");

            for(JsonNode game : games) {
                int appId = game.path("appid").asInt();
                String priority = game.path("priority").asString();

                String date =game.path("date_added").asString();

                wishlistList.add(new WishlistModel(appId,priority,date));
            }

        }catch (Exception e) {
            logger.error("[PARSE-003] Failed to parse Wishlist Games", e);
            return null;
        }
        return wishlistList;
    }
}
