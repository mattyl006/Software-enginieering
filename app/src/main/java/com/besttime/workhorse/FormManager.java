package com.besttime.workhorse;

import android.os.Debug;
import android.util.Log;

import com.besttime.workhorse.helpers.ParsedRowFromSheet;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class FormManager implements Serializable {

    private List<Form> sentForms;
    private transient SheetsAndJava dataRetriever;

    public void setSmsManager(SmsManager smsManager) {
        this.smsManager = smsManager;
    }

    private transient SmsManager smsManager;

    public static final String smsMessage = "Hej, zaznacz kiedy jesteś dostępny abym mógł do Ciebie zadzwonić: \n";
    public static final String formUrl = "https://docs.google.com/forms/d/e/1FAIpQLScEiidNdn1cDF3GkJ1Gaysv5fnDebM4BjWzMuRoNDGF6jTKQQ/viewform?usp=pp_url&entry.1877286907=";

    public List<Form> getSentForms() {
        return sentForms;
    }

    /**
     *
     * @throws IOException When there was error creating forms data retriever
     * @throws GeneralSecurityException When there was error creating forms data retriever
     * @param smsManager
     */
    public FormManager(SmsManager smsManager) throws IOException, GeneralSecurityException {
        this.smsManager = smsManager;
        this.sentForms = new ArrayList<>();
        dataRetriever = new SheetsAndJava(smsManager.getAndroidContext());
    }

    private String makeSmsMessage(Form form){
        return smsMessage + formUrl + form.getContext().getContact().getContactId();
    }


    public void sendForm(Form form){
        smsManager.sendSmsPrompt(form.getContext(), makeSmsMessage(form));
    }


    /**
     * Checks whether some of the forms has been filled, fetches answers for these forms and updates them.
     * @throws IOException When there was error checking responses
     * @throws ParseException Error parsing row
     */
    public void checkResponses() throws IOException, ParseException {

        List<List<Object>> dataFromSheets = dataRetriever.getAllFormsAnswers();

        List<ParsedRowFromSheet> parsedRowsFromSheet = new ArrayList<>();

        for (List<Object> notParsedRowFromSheet :
                dataFromSheets) {
            parsedRowsFromSheet.add(new ParsedRowFromSheet(notParsedRowFromSheet));
        }


        // Iterate over all parsed rows and check if there are some forms updated and update them
        for (ParsedRowFromSheet parsedRow :
                parsedRowsFromSheet) {

            for (Form sentForm :
                    sentForms) {

                if(sentForm.getId() == parsedRow.getFormId()){
                    if(!sentForm.hasResponded()){
                        sentForm.updateAllDaysAnswers(parsedRow.getAnswers(), parsedRow.getDateWhenFormFilled());
                    }
                    else{
                        if(sentForm.getDateWhenResponded().before(parsedRow.getDateWhenFormFilled())){
                            sentForm.updateAllDaysAnswers(parsedRow.getAnswers(), parsedRow.getDateWhenFormFilled());
                        }
                    }
                }

            }
        }

    }


    public void addNewFormToSentForms(Form newForm){
        sentForms.add(newForm);
    }


    /**
     *
     * @param formId
     * @return null if form hasn't been filled yet.
     */
    public Form getSentFormWithId(Long formId){
        for (Form form :
                sentForms) {
            if(formId == form.getId()){
                return form;
            }
        }

        return null;
    }


}