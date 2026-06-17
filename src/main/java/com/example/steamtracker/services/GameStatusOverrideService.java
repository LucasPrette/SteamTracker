package com.example.steamtracker.services;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.enums.GameStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameStatusOverrideService {
    private final SheetsClient sheetsClient;
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");
    private static final Logger logger = LoggerFactory.getLogger(GameStatusOverrideService.class);



    public Map<Integer, GameStatus> getOverrideMap(){
        try{
            Map<Integer, GameStatus> overrides =
                    new HashMap<>();

            var response = sheetsClient.getValues(SPREADSHEET_ID,
                    "Game_Status!A2:B");

            if (response == null
                    || response.getValues() == null) {
                return overrides;
            }

            for(List<Object> row : response.getValues()) {

                if(row.size() < 2) {
                    continue;
                }

                int appId  = Integer.parseInt(row.get(0).toString());

                GameStatus status =
                        GameStatus.valueOf(
                                row.get(1).toString()
                        );
                overrides.put(appId, status);

            }
            return overrides;

        } catch (Exception e) {
            logger.error("Failed to get status override");
        }

        return null;
    }

    public void saveOverride(
            int appId,
            GameStatus status
    ) {

    }
}
