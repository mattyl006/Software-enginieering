package com.besttime.workhorse;

import android.util.Log;

import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class SheetsAndJavaTest {

    @Test
    public void getAllFormsAnswers() throws IOException, GeneralSecurityException, ParseException {
        SheetsAndJava dataRetriever = new SheetsAndJava();

        List<List<Object>> allRows = dataRetriever.getAllFormsAnswers();

        for (List<Object> row :
                allRows) {
            for (Object cell :
                    row) {
                assertEquals(cell.getClass(), String.class);
            }

            try {
                Date dateFilled = SheetsAndJava.dateFormat.parse((String) row.get(0));
            } catch (ParseException e) {
                Log.d("SheetsAndJavaTest", "No date or wrong date format in sheet");
                throw e;
            }

            String formIdAsString = (String) row.get(row.size() - 1);
            long formId = Long.parseLong(formIdAsString);
        }
    }
}