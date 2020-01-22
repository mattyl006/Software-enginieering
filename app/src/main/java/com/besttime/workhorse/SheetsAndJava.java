package com.besttime.workhorse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class SheetsAndJava {
    private Sheets sheetsService;
    private final String APPLICATION_NAME = "Availability Sheet";
    private final String SPREADSHEET_ID = "1vFmyVumydY92lfRME4bpROJ_7WrwH6yrGXBb8FZpJCc";

    private final String range = "dostepnosc!A2:I100000";

    private Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = SheetsAndJava.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");
        return credential;
    }

    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public SheetsAndJava() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
    }


    /**
     *
     * @return List of rows from spreadsheet. Row is a list of Strings containing:
     * - date when form was filled
     * - form id
     * - answers about availability
     * @throws IOException When there was error retrieving rows from spreadsheet
     */
    public List<List<Object>> getAllFormsAnswers() throws IOException {
        ValueRange response = null;
        try {
            response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        List<List<Object>> rows = null;
        if(response != null){
            rows = response.getValues();
        }

        return rows;
    }

}
