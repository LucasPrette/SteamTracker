package com.example.steamtracker.clients;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class SteamClient {

    private final String API_KEY = System.getenv("STEAM_API_KEY");
    private final String STEAM_ID = System.getenv("STEAM_ID");

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

}
