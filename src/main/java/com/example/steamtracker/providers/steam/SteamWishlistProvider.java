package com.example.steamtracker.providers.steam;

import com.example.steamtracker.clients.StoreClient;
import com.example.steamtracker.entities.Game;
import com.example.steamtracker.entities.WishListEntry;
import com.example.steamtracker.enums.Platform;
import com.example.steamtracker.models.WishlistModel;
import com.example.steamtracker.providers.WishlistProvider;
import com.example.steamtracker.services.Steam.SteamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SteamWishlistProvider implements WishlistProvider {

    private final SteamService steamService;
    private final StoreClient storeClient;

    @Override
    public List<WishListEntry> getWishlist() {
        String json = storeClient.getWishlist();

        List<WishlistModel> currentWishlist =
                steamService.parseWishlist(json);

        return currentWishlist.stream()
                .map(item -> new WishListEntry(
                        new Game(
                                item.getAppId(),
                                null,
                                Platform.STEAM
                        )
                ))
                .toList();
    }
}
