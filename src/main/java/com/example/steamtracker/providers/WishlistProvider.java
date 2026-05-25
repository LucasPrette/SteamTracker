package com.example.steamtracker.providers;

import com.example.steamtracker.entities.Game;

import java.util.List;

public interface WishlistProvider {
    List<Game> getWishlist();
}
