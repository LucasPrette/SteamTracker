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
    private final HttpClient httpClient =
            HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

    public String getPlayerAchievements(int appId) {
        for(int attempt = 1; attempt <= 3; attempt ++) {
            try {
                String url = "https://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v1/"
                        + "?appid=" + appId
                        + "&key=" + API_KEY +
                        "&steamid=" + STEAM_ID;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();

            } catch (Exception e) {
                logger.warn("[STEAM-001] Attempt {} Failed to get player achievements.", attempt);

                if (attempt == 3) {
                    logger.error("[STEAM-001] Failed to get player achievements."
                            , e);
                }

                try{
                    Thread.sleep(2000);
                }catch (InterruptedException ignored){

                }
            }
        }
        return null;
    }

    public String getRecentPlayedGames() {
        for(int attempt = 1; attempt <= 3; attempt ++) {
            try{
                String url = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/"
                        + "?key=" + API_KEY
                        + "&steamid=" + STEAM_ID;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();

            } catch (Exception e) {
                logger.warn("[STEAM-002] Attempt {} failed to get Recent Played games.", attempt);

                if(attempt == 3) {
                    logger.error("[STEAM-002] Failed to get recent played games.", e);
                }
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException ignored) {
                }
            }
        }
        return null;
    }

    public String getAllGames() {
        for(int attempt = 1; attempt <= 3; attempt ++){
            try{
                String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/" +
                        "?key=" + API_KEY +
                        "&steamid="+ STEAM_ID + "&include_appinfo=True";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();

            } catch (Exception e){
                logger.warn("[STEAM-003] Attempt {} failed fetching owned games", attempt);

                if(attempt == 3) {
                    logger.error(
                            "[STEAM-003] Failed to get owned games after retries",
                            e
                    );
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        return null;
    }

}
