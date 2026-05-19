package com.example.steamtracker.clients;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Component;
import com.google.auth.http.HttpCredentialsAdapter;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class SheetsClient {
    private Sheets sheetsService;

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
            e.printStackTrace();
        }
    }
    public void writeLocal(String spreadSheetId, String range, List<List<Object>> values){
        try{
            ValueRange body = new ValueRange().setValues(values);

            sheetsService.spreadsheets().values().update(spreadSheetId, range, body).setValueInputOption("RAW").execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ValueRange getValues(
            String spreadSheetId,
            String range
    ){
        try{
            return sheetsService.spreadsheets().values().get(spreadSheetId,range).execute();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            e.printStackTrace();
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
            System.err.println("Clearing row not possible...");
            e.printStackTrace();
        }
    }
}

