package com.besttime.workhorse;

import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.junit.Assert.*;

public class SheetsAndJavaTest {

    @Test
    public void getAllFormsAnswers() throws IOException, GeneralSecurityException {
        SheetsAndJava dataRetriever = new SheetsAndJava();

        List<List<Object>> allRows = dataRetriever.getAllFormsAnswers();

        for (List<Object> row :
                allRows) {
            for (Object cell :
                    row) {
                assertEquals(cell.getClass(), String.class);
            }
        }
    }
}