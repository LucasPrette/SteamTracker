package com.example.steamtracker.services.sheet;

import com.example.steamtracker.clients.SheetsClient;
import com.example.steamtracker.models.GamingAdvice;
import com.example.steamtracker.services.GamingAdvisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GamingAdvisorSheetService {
    private final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");
    private final GamingAdvisorService gamingAdvisorService;
    private final SheetsClient sheetsClient;

    public void syncGamingAdvisor() {
        List<GamingAdvice> advice =
                gamingAdvisorService.generateAdvice();


        List<List<Object>> values = advice.stream()
                .map(item -> List.<Object>of(
                        item.getAction(),
                        item.getGameName(),
                        item.getReason()
                ))
                .toList();


        sheetsClient.clearRange(
                SPREADSHEET_ID,
                "Gaming_Advisor!A2:C"
        );

        if(!values.isEmpty()) {
            sheetsClient.writeLocal(
                    SPREADSHEET_ID,
                    "Gaming_Advisor!A2",
                    values
            );
        }

    }
}
