package com.besttime.workhorse;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

public class SheetsAndJava {
    private static final String TOKENS_DIRECTORY_PATH = "/bestTime/tokensDirectory";
    private Sheets sheetsService;
    private final String APPLICATION_NAME = "Availability Sheet";
    private final String SPREADSHEET_ID = "1vFmyVumydY92lfRME4bpROJ_7WrwH6yrGXBb8FZpJCc";

    public static final String google_api_key = "AIzaSyCEBWOxFJMu2zHUHwnk4KHU8WNfmVI32Do";


    private List<List<Object>> allRows = null;

    // TEMPORARY CHANGE OF RANGE, UNTIL WRONG DATA IS REMOVED FROM SHEET
    private final String range = "dostepnosc!A11:I100000";
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Context androidContext;


    public SheetsAndJava(@Nullable Context androidContext)  {
        this.androidContext = androidContext;
        sheetsService = getSheetsService();
    }

    private Sheets getSheetsService() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory factory = JacksonFactory.getDefaultInstance();
        final Sheets sheetsService = new Sheets.Builder(transport, factory, null)
                .setApplicationName("My Awesome App")
                .build();

        return sheetsService;
    }


    /**
     *
     * @return List of rows from spreadsheet. Row is a list of Strings containing:
     * - date when form was filled
     * - form id
     * - answers about availability
     */
    public List<List<Object>> getAllFormsAnswers()  {

        allRows =null;

        try {
            allRows =  task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return allRows;


    }











     AsyncTask<Void, Void, List<List<Object>>> task = new AsyncTask<Void, Void, List<List<Object>>>() {

        @Override
        protected List<List<Object>> doInBackground(Void... params) {
            ValueRange response = null;
            try {
                response = sheetsService.spreadsheets().values()
                        .get(SPREADSHEET_ID, range)
                        .setKey(google_api_key)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<List<Object>> rows = null;
            if(response != null){
                rows = response.getValues();
            }

            allRows = rows;
            return rows;
        }



        @Override
        protected void onPostExecute(List<List<Object>> lists) {
            super.onPostExecute(lists);
        }
    };

}



