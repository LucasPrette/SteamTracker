package com.example.steamtracker.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class SteamClient {

    private final String API_KEY = System.getenv("STEAM_API_KEY");
    private final String STEAM_ID = System.getenv("STEAM_ID");
    private static final Logger logger = LoggerFactory.getLogger(SteamClient.class);

    public String getPlayerAchievements(int appId) {
        try {
            String url = "https://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v1/"
                    + "?appid=" + appId
                    + "&key=" + API_KEY +
                    "&steamid=" + STEAM_ID;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            logger.error("[STEAM-001] Failed to get player achievements...", e);

            return null;
        }
    }

    public String getRecentPlayedGames() {
        try{
            String url = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/"
                    + "?key=" + API_KEY
                    + "&steamid=" + STEAM_ID;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            logger.error("[STEAM-002] Failed to get Recent Played games ...", e);
            return null;
        }
    }

    public String getAllGames() {
        try{
            String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/" +
                    "?key=" + API_KEY +
                    "&steamid="+ STEAM_ID + "&include_appinfo=True";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e){
            logger.error("[STEAM-003] Failed to get owned games...", e);
            return null;
        }
    }

}
