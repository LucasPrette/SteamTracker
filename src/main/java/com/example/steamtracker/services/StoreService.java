package com.example.steamtracker.services;

import com.example.steamtracker.models.GamePrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class StoreService {
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);
    public GamePrice parsePrice(String json, int appId){

        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode gameNode = root.path(String.valueOf(appId));

            if(!gameNode.path("success").asBoolean()){
                return null;
            }

            JsonNode data = gameNode.path("data");

            String name = data.path("name").asString();

            JsonNode priceOverview = data.path("price_overview");

            if(priceOverview.isMissingNode()){
                return null;
            }

            double finalPrice = priceOverview.path("final").asDouble() / 100.0;
            double originalPrice = priceOverview.path("initial").asDouble() / 100.0;
            Double discount = priceOverview.path("discount_percent").asDouble();
            return new GamePrice(name, finalPrice, originalPrice, discount);

        }catch (Exception e){
                logger.error("[PARSE-004] Failed to Parse Price", e);
            return null;
        }
    }
}
