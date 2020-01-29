package com.besttime.workhorse;

import android.os.Debug;
import android.util.Log;

import com.besttime.app.ContactEntry;
import com.besttime.workhorse.helpers.ParsedRowFromSheet;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FormManager implements Serializable {

    private List<Form> sentForms;
    private transient SheetsAndJava dataRetriever;

    private transient SmsManager smsManager;

    public static final String smsMessage = "Hej, zaznacz kiedy jesteś dostępny abym mógł do Ciebie zadzwonić: \n";
    public static final String remindingSmsMessage = "Hej, przypominam, że w każdej chwili możesz zmienić godziny swojej dostępności do rozmowy:\n";
    public static final String formUrl = "https://docs.google.com/forms/d/e/1FAIpQLScEiidNdn1cDF3GkJ1Gaysv5fnDebM4BjWzMuRoNDGF6jTKQQ/viewform?usp=pp_url&entry.1877286907=";



    public static final String JSON_NAME = "formManager";

    public List<Form> getSentForms() {
        return sentForms;
    }

    /**
     *
     * @throws IOException When there was error creating forms data retriever
     * @throws GeneralSecurityException When there was error creating forms data retriever
     */
    public FormManager() throws IOException, GeneralSecurityException {
        this.sentForms = new ArrayList<>();
    }

    public void setTransientFields(SmsManager smsManager){
        this.smsManager = smsManager;
        dataRetriever = new SheetsAndJava(smsManager.getAndroidContext());
    }

    private String makeSmsMessage(Form form){
        return smsMessage + formUrl + form.getContext().getContact().getContactId();
    }


    public void sendForm(Form form){
        smsManager.sendSmsPrompt(form.getContext(), makeSmsMessage(form));
    }

    public void sendRemindersThatFormCanBeUpdated(){
        for (Form sentForm :
                sentForms) {
            if(sentForm.hasResponded()){

                Date respondDate = sentForm.getDateWhenResponded();
                Calendar respondDateCalendar = Calendar.getInstance();
                respondDateCalendar.setTime(respondDate);

                Date currentdate = new Date();
                Calendar currentDateCalendar = Calendar.getInstance();
                currentDateCalendar.setTime(currentdate);

                int dayOfYearWhenResponded = respondDateCalendar.get(Calendar.DAY_OF_YEAR);
                int dayOfYearNow = currentDateCalendar.get(Calendar.DAY_OF_YEAR);

                if((dayOfYearWhenResponded - dayOfYearNow) >= 30){
                    smsManager.sendSmsPrompt(sentForm.getContext(), remindingSmsMessage + formUrl + sentForm.getContext().getContact().getContactId());
                }

            }
        }
    }


    /**
     * Sends reminder to contact, that he/she can fill form again to update his availability time.
     * @param contactToSendTo
     * @return True if form to this contact has already been sent before and reminder now can be sent, false if no sms was sent before with form url.
     */
    public boolean sendReminderThatFormCanBeUpdated(ContactEntry contactToSendTo){


        for (Form sentForm:
             sentForms) {
            if(sentForm.getContext().getContact().getContactId() == contactToSendTo.getContactId()){
                smsManager.sendSmsPrompt(sentForm.getContext(), remindingSmsMessage + formUrl + sentForm.getContext().getContact().getContactId());
                return true;
            }
        }


        return false;

    }


    /**
     * Checks whether some of the forms has been filled, fetches answers for these forms and updates them.
     * @throws IOException When there was error checking responses
     * @throws ParseException Error parsing row
     */
    public void checkResponses(List<ContactEntry> contactEntries) throws ParseException {


        if(contactEntries != null){
             if(!contactEntries.isEmpty()){

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

                                 for (ContactEntry contactEntry :
                                         contactEntries) {
                                     if(contactEntry.getContactId() == sentForm.getId()){
                                         QuerySmsComputation querySmsComputation = new QuerySmsComputation(sentForm.generateResult(), new Week());
                                         contactEntry.getAvailability().setAvailability(querySmsComputation.getWeek());
                                     }
                                 }

                             }
                             else{
                                 if(sentForm.getDateWhenResponded().before(parsedRow.getDateWhenFormFilled())){
                                     sentForm.updateAllDaysAnswers(parsedRow.getAnswers(), parsedRow.getDateWhenFormFilled());
                                     for (ContactEntry contactEntry :
                                             contactEntries) {
                                         if(contactEntry.getContactId() == sentForm.getId()){
                                             QuerySmsComputation querySmsComputation = new QuerySmsComputation(sentForm.generateResult(), new Week());
                                             contactEntry.getAvailability().setAvailability(querySmsComputation.getWeek());
                                         }
                                     }
                                 }
                             }
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