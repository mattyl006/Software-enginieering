package com.besttime.workhorse.helpers;

import com.besttime.workhorse.DayOfTheWeek;
import com.besttime.workhorse.Hours;
import com.besttime.workhorse.SheetsAndJava;

import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ParsedRowFromSheetTest {

    public List<Object> sampleNotParsedRow =
            Arrays.asList(new Object[]{"2020-01-21 17:52:26", "9:00 1", "22:00 1", "6:00 2", "Nie mogę rozmawiać w ten dzień 3", "Nie mogę rozmawiać w ten dzień 4", "18:30 5", "20:00 5", "16:30 6", "18:00 6", "12:00 0", "16:00 0", "11"});






    @Test
    public void getFormId() throws ParseException {
        ParsedRowFromSheet parsedRowFromSheet = initializeRow(sampleNotParsedRow);

        long expectedFormdId = 11;

        assertEquals(expectedFormdId, parsedRowFromSheet.getFormId());

    }

    @Test
    public void getDateWhenFormFilled() throws ParseException {
        ParsedRowFromSheet parsedRowFromSheet = initializeRow(sampleNotParsedRow);

        Date expectedDate = SheetsAndJava.dateFormat.parse((String) sampleNotParsedRow.get(0));


        assertEquals(expectedDate, parsedRowFromSheet.getDateWhenFormFilled());

    }

    @Test
    public void getAnswers() throws ParseException {
        ParsedRowFromSheet parsedRowFromSheet = initializeRow(sampleNotParsedRow);

        List<DayOfTheWeek> expectedAnswers = initializeExpectedAnswers();


        List<DayOfTheWeek> actualAnswers = parsedRowFromSheet.getAnswers();

        // Iterate over all expected answers and compare them with current answers
        for (DayOfTheWeek expectedAnswer :
                expectedAnswers) {

            for (DayOfTheWeek actualAnswer :
                    actualAnswers) {
                if(actualAnswer.getId() == expectedAnswer.getId()){




                    Map<Hours, Integer> expectedHours = expectedAnswer.getMap();
                    Map<Hours, Integer> currentHours = actualAnswer.getMap();

                    // Compare all hours and their points for current day (eg. for Monday)
                    for (Hours hour :
                            expectedHours.keySet()) {
                        long expectedPoints = expectedHours.get(hour);
                        long actualPoints = currentHours.get(hour);

                        if(expectedPoints != actualPoints){
                            int a = 1;
                            a ++;
                        }

                        assertEquals(expectedPoints, actualPoints);
                    }

                }
            }
        }


    }

    private List<DayOfTheWeek> initializeExpectedAnswers() {
        List<DayOfTheWeek> expectedAnswers = new ArrayList<>();


        DayOfTheWeek monday = new DayOfTheWeek(1);
        monday.loadOneTime(9, 0);
        monday.loadOneTime(22, 0);
        expectedAnswers.add(monday);

        DayOfTheWeek tuesday = new DayOfTheWeek(2);
        tuesday.loadOneTime(6, 0);
        expectedAnswers.add(tuesday);

        DayOfTheWeek wednesday = new DayOfTheWeek(3);
        wednesday.loadDayToBeUndefined();
        expectedAnswers.add(wednesday);

        DayOfTheWeek thursday = new DayOfTheWeek(4);
        thursday.loadDayToBeUndefined();
        expectedAnswers.add(thursday);


        DayOfTheWeek friday = new DayOfTheWeek(5);
        friday.loadOneTime(18, 30);
        friday.loadOneTime(20, 0);
        expectedAnswers.add(friday);


        DayOfTheWeek saturday = new DayOfTheWeek(6);
        saturday.loadOneTime(16, 30);
        saturday.loadOneTime(18, 0);
        expectedAnswers.add(saturday);


        DayOfTheWeek sunday = new DayOfTheWeek(0);
        sunday.loadOneTime(12, 0);
        sunday.loadOneTime(16, 0);
        expectedAnswers.add(sunday);
        return expectedAnswers;
    }


    private ParsedRowFromSheet initializeRow(List<Object> notParsedRow) throws ParseException {
        ParsedRowFromSheet parsedRowFromSheet = null;
        try {
            parsedRowFromSheet = new ParsedRowFromSheet(sampleNotParsedRow);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }

        return parsedRowFromSheet;
    }
}