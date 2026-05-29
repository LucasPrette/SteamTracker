package com.example.steamtracker.providers;

import com.example.steamtracker.entities.WishListEntry;

import java.util.List;

public interface WishlistProvider {
    List<WishListEntry> getWishlist();
}
