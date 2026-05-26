package com.example.steamtracker.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class StoreClient {
    private static final Logger logger = LoggerFactory.getLogger(StoreClient.class);
    private final HttpClient httpClient =
            HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

    public String getGameDetails(int appId){
        for(int attempt = 1; attempt <= 3; attempt ++) {
            try{
                String url = "https://store.steampowered.com/api/appdetails?appids="
                        + appId
                        + "&cc=br&l=portuguese";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(10))
                        .GET().build();

                HttpResponse<String> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();

            }catch (Exception e){
                logger.warn("[STEAM-004] Attempt {} Failed to get game information", attempt);

                if(attempt == 3){
                    logger.error("[STEAM-004] Failed to get game information", e);
                }

                try{
                    Thread.sleep(2000);
                }catch (InterruptedException ignored){

                }
            }
        }
        return null;
    }

    public String searchGame(int appId) {
        for(int attempt = 1; attempt <= 3; attempt ++){
            try{
                String url = "https://store.steampowered.com/api/storesearch/?term=" +
                        appId + "&cc=br&l=portuguese";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(10))
                        .GET().build();

                HttpResponse<String> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            } catch (Exception e){
                logger.warn("[STEAM-005] Attempt {} failed to find game",attempt);

                if(attempt == 3) {
                    logger.error("[STEAM-005] Failed to find game", e);
                }

                try{
                    Thread.sleep(2000);
                }catch (InterruptedException ignored) {

                }
            }
        }
        return null;
    }

    public String getWishlist() {
        for(int attempt = 1; attempt <= 3; attempt ++) {
            try{
                String url = "https://api.steampowered.com/IWishlistService/GetWishlist/v1/" +
                        "?key="+ System.getenv("STEAM_API_KEY") +
                        "&steamid="+ System.getenv("STEAM_ID");

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .timeout(Duration.ofSeconds(10))
                                .build();

                HttpResponse<String> response = httpClient
                        .send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            } catch (Exception e) {
                logger.warn("[STEAM-006] Attempt {} failed to get wishlist games", attempt);

                if(attempt == 3){
                    logger.error("[STEAM-006] Failed to get wishlist games", e);
                }
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {

                }
            }
        }
        return null;
    }
}
