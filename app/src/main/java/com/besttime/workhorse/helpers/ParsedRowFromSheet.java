package com.besttime.workhorse.helpers;

import com.besttime.workhorse.DayOfTheWeek;
import com.besttime.workhorse.Hours;
import com.besttime.workhorse.SheetsAndJava;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParsedRowFromSheet {

    private long formId;
    private Date dateWhenFormFilled;
    private List<DayOfTheWeek> answers = new ArrayList<>();

    public ParsedRowFromSheet(List<Object> notParsedRow) throws ParseException {

        for(int i = 0; i < 7; i ++){
            answers.add(new DayOfTheWeek(i));
        }

        dateWhenFormFilled = SheetsAndJava.dateFormat.parse((String) notParsedRow.get(0));

        formId = Long.parseLong((String) notParsedRow.get(notParsedRow.size() - 1));

        List<String> notParsedAnswers = new ArrayList<>();

        // i = 0 - date, i = size - formId
        for(int i = 1; i < notParsedRow.size() - 1; i ++){

            String[] splitAnswers = ((String)notParsedRow.get(i)).split(",");
            for (String answer :
                    splitAnswers) {
                notParsedAnswers.add(answer);
            }
        }


        parseAnswers(notParsedAnswers);
    }


    /**
     *
     * @param notParsedAnswers ["hour:min dayNum", "hour:min dayNum", ... , "hour:min dayNum"]
     */
    private void parseAnswers(List<String> notParsedAnswers){
        for (String notParsedAnswer :
                notParsedAnswers) {

            // 0 - Sunday, 1 - Monday
            int dayNumber = Integer.parseInt(notParsedAnswer.substring(notParsedAnswer.length() - 1));


            // answer was "Nie moge rozmawiac ... dayNumber"
            if (notParsedAnswer.contains("N")) {
                getDayWithId(dayNumber).loadDayToBeUndefined();
            }
            else{
                String notParsedTime = notParsedAnswer.substring(0, notParsedAnswer.length() - 1);
                Hours parsedHour = parseStringTimeToHoursEnum(notParsedTime);
                getDayWithId(dayNumber).loadOneTime(parsedHour);
            }
        }
    }


    private DayOfTheWeek getDayWithId(int dayId){
        for (DayOfTheWeek day :
                answers) {
            if (day.getId() == dayId) {
                return day;
            }
        }

        return null;
    }


    /**
     *
     * @param notParsedTime String in format "hour:minutes {whitespace}"
     * @return
     */
    private Hours parseStringTimeToHoursEnum(String notParsedTime){

        int indexOfColon = notParsedTime.indexOf(":");
        String hourAsString = notParsedTime.substring(0, indexOfColon).trim();
        String minutesAsString = notParsedTime.substring(indexOfColon + 1, notParsedTime.length()).trim();

        double hour = Integer.parseInt(hourAsString);
        double minutes = Integer.parseInt(minutesAsString);

        double parsedHour = hour + (minutes / 100);

        for (Hours hourEnum:
             Hours.values()) {
            if(parsedHour == hourEnum.getHourValue()){
                return hourEnum;
            }
        }

        return null;
    }

    public long getFormId() {
        return formId;
    }

    public Date getDateWhenFormFilled() {
        return dateWhenFormFilled;
    }

    public List<DayOfTheWeek> getAnswers() {
        return answers;
    }
}
