package com.example.steamtracker.providers.steam;

import com.example.steamtracker.clients.StoreClient;
import com.example.steamtracker.entities.GamePriceOffer;
import com.example.steamtracker.models.GamePrice;
import com.example.steamtracker.providers.PriceProvider;
import com.example.steamtracker.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SteamPriceProvider implements PriceProvider {

    private final StoreClient storeClient;
    private final StoreService storeService;
    private final Logger logger = LoggerFactory.getLogger(SteamPriceProvider.class);


    @Override
    public GamePriceOffer getPrice(int externalId) {

        String json = storeClient.getGameDetails(externalId);

        GamePrice price = storeService.parsePrice(json, externalId);

        if (price == null) {

            logger.warn(
                    "[STEAM-007] Price parsing returned null for appId {}",
                    externalId
            );

            return null;
        }

        GamePriceOffer offer = new GamePriceOffer();

        System.out.println(price.getName());
        System.out.println(externalId);

        offer.setProviderName("STEAM");
        offer.setGameName(price.getName());
        offer.setFinalPrice(price.getFinalPrice());
        offer.setOriginalPrice(price.getOriginalPrice());
        offer.setDiscount(price.getDiscount());

        return offer;
    }
}
