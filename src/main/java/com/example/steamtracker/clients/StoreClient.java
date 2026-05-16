package com.example.steamtracker.clients;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class StoreClient {

    public String getGameDetails(int appId){
        try{
            String url = "https://store.steampowered.com/api/appdetails?appids="
                    + appId
                    + "&cc=br&l=portuguese";

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String searchGame(int appId) {
        try{
            String url = "https://store.steampowered.com/api/storesearch/?term=" +
                    appId + "&cc=br&l=portuguese";

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getWishlist() {
        try{
            String url = "https://api.steampowered.com/IWishlistService/GetWishlist/v1/" +
                    "?key="+ System.getenv("STEAM_API_KEY") +
                    "&steamid="+ System.getenv("STEAM_ID");
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
