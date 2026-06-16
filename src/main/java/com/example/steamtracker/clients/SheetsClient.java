package com.example.steamtracker.clients;

import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.google.auth.http.HttpCredentialsAdapter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;

@Component
public class SheetsClient {
    private Sheets sheetsService;
    private static final Logger logger = LoggerFactory.getLogger(SheetsClient.class);

    public SheetsClient() {
        try {
            InputStream credentialsStream =
                    new FileInputStream(
                            System.getProperty("user.home")
                                    + "/credentials/steamsync-495415-54fb9885729f.json"
                    );
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

            sheetsService = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("Steam Sync")
                    .build();

        } catch (Exception e) {
            logger.error("[SHEET-001] Authentication failed", e);
        }
    }

    public void batchWrite(
            String spreadsheetId,
            List<ValueRange> updates
    ){
        try{
            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                    .setValueInputOption("RAW")
                    .setData(updates);

            sheetsService.spreadsheets()
                    .values()
                    .batchUpdate(
                            spreadsheetId,
                            body
                    )
                    .execute();
        } catch (Exception e) {
            logger.error("[SHEET-007 Failed to batch write updates");
        }

    }
    public void writeLocal(String spreadSheetId, String range, List<List<Object>> values){
        for(int attempt = 1; attempt <= 3; attempt ++) {
            try{
                ValueRange body = new ValueRange().setValues(values);

                sheetsService.spreadsheets().values().update(
                        spreadSheetId,
                        range,
                        body).setValueInputOption("RAW").execute();

            }catch (Exception e){
                logger.warn("[SHEET-002] Attempt {} failed to write locally on sheet",attempt);

                if (attempt == 3) {
                    logger.error("[SHEET-002] Failed to write locally on sheet...", e);
                }

                try{
                    Thread.sleep(2000);
                }catch (InterruptedException ignored){

                }
            }
        }

    }

    public ValueRange getValues(
            String spreadSheetId,
            String range
    ){
        for(int attemmpt = 1; attemmpt <= 3; attemmpt ++ ){
            try{
                return sheetsService.spreadsheets()
                        .values()
                        .get(spreadSheetId, range).execute();

            }catch (Exception e) {
                logger.warn("[SHEET-003] Attempt {} failed to get values from spreadsheet", attemmpt);

                if(attemmpt == 3){
                    logger.error("[SHEET-003] Failed to get values from spreadsheet", e);
                }

                try{
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {

                }
            }
        }

        return null;
    }

    public void appendToSheet(
            String spreadsheetId,
            String range,
            List<List<Object>> values
    ) {
        try {
            ValueRange body = new ValueRange().setValues(values);

            sheetsService.spreadsheets().values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (Exception e) {
            logger.error("[SHEET-004] Appending to sheet failed...", e);

        }
    }

    public void clearRow(String spreadsheetId, String range) {
        try{
            ValueRange body = new ValueRange()
                    .setValues(List.of(List.of("","","","")));

            sheetsService.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();

        }catch (Exception e) {
            logger.error("[SHEET-005] Clearing row not possible...",e);

        }
    }

    public void clearRange(String spreadsheetId, String range){
        try{
            sheetsService.spreadsheets().values().clear(
                    spreadsheetId,
                    range,
                    new ClearValuesRequest()).execute();

        } catch (Exception e) {
            logger.error("[SHEET-006] Failed trying to clear Sheet range selection...",e);

        }
    }
}

