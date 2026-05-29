package com.example.steamtracker.providers;

import com.example.steamtracker.entities.GamePriceOffer;

public interface PriceProvider {
    GamePriceOffer getPrice(int externalId);
}
