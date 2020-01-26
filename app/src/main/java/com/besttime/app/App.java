package com.besttime.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.besttime.app.helpers.ContactImporter;
import com.besttime.app.helpers.WhatsappCallPerformable;
import com.besttime.app.helpers.WhatsappRedirector;
import com.besttime.json.Json;
import com.besttime.models.Contact;
import com.besttime.ui.helpers.WhatsappContactIdRetriever;
import com.besttime.workhorse.CurrentTime;
import com.besttime.workhorse.Form;
import com.besttime.workhorse.FormManager;
import com.besttime.workhorse.SmsManager;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App implements Serializable, WhatsappCallPerformable {

    public final static String nameToDeserialize = "app";

    public static final int PERMISSIONS_REQUEST_SEND_SMS = 121;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 199;
    public static final int PERMISSIONS_PHONE_CALL = 197;

    public static final int WHATSAPP_VIDEO_CALL_REQUEST = 11;


    private boolean firstUse;
    private List<String> contactListJsonNames;
    private transient List<ContactEntry> contactEntries = new ArrayList<>();
    private Date lastLaunch;
    private transient FormManager formManager;
    private transient Json json;

    private transient Context androidContext;

    private transient ContactImporter contactImporter;
    private transient WhatsappRedirector whatsappRedirector;
    private transient WhatsappContactIdRetriever whatsappContactIdRetriever;

    private transient boolean doingCall;


    /**
     *
     * @param androidContext
     * @throws IOException
     * @throws GeneralSecurityException When there was error creating formManager
     */
    public App(final Context androidContext) throws IOException, GeneralSecurityException, ParseException, ClassNotFoundException {
        this.androidContext = androidContext;
        contactListJsonNames = new ArrayList<>();

        json = new Json(androidContext);

        firstUse = false;

        setAllTransientFields(androidContext);

    }

    public void importContacts() {
        List<Contact> contacts = contactImporter.getAllContacts();


        contactEntries = new ArrayList<>();
        for (Contact contact :
                contacts) {
            ContactEntry newContactEntry = new ContactEntry(contact);
            String nameToSerialize = newContactEntry.getContactId() + newContactEntry.getContactNumber();
            if(!contactListJsonNames.contains(nameToSerialize)){
                contactListJsonNames.add(nameToSerialize);
                json.serialize(nameToSerialize, newContactEntry);
                contactEntries.add(newContactEntry);
            }
        }
    }

    /**
     * Sets formManager if it has been null, does nothing otherwise.
     * @param formManager
     */
    public void setFormManager(FormManager formManager) {
        if(formManager == null){
            this.formManager = formManager;
        }
    }


    public void setAllTransientFields(Context androidContext) throws IOException, GeneralSecurityException, ParseException, ClassNotFoundException {
        this.androidContext = androidContext;
        json = new Json(androidContext);
        contactImporter = new ContactImporter(PERMISSIONS_REQUEST_READ_CONTACTS, androidContext);
        whatsappContactIdRetriever =new WhatsappContactIdRetriever(androidContext.getContentResolver());
        whatsappRedirector = new WhatsappRedirector(androidContext, whatsappContactIdRetriever);

        try {
            formManager = (FormManager) json.deserialize(FormManager.JSON_NAME);
        }
        catch (IOException e){
            formManager = new FormManager();
        }

        formManager.setTransientFields(new SmsManager(androidContext, PERMISSIONS_REQUEST_SEND_SMS));

        contactEntries = deserializeAllContacts();


        formManager.checkResponses(contactEntries);

        for (ContactEntry contactEntry :
                contactEntries) {
            json.serialize(contactEntry.getContactId() + contactEntry.getContactNumber(), contactEntry);
        }

        json.serialize(FormManager.JSON_NAME, formManager);


    }




    public List<ContactEntry> getContactList() throws IOException, ClassNotFoundException {
        return contactEntries;
    }

    private List<ContactEntry> deserializeAllContacts() throws IOException, ClassNotFoundException {
        List<ContactEntry> result = new ArrayList<>();

        for (String nameToDeserialize :
                contactListJsonNames) {
            result.add((ContactEntry) json.deserialize(nameToDeserialize));
        }

        return result;
    }



    public void appLoop(){

    }

    public void updateContactList(){

    }


    private ContactEntry lastCalledContact;

    public void whatsappForward(ContactEntry contact){
        if(!doingCall){
            whatsappRedirector.redirectToWhatsappVideoCall(contact, WHATSAPP_VIDEO_CALL_REQUEST);
            lastCalledContact = contact;
            doingCall = true;
        }
    }

    public void onWhatsappVideoCallEnd(boolean wasCallAnwsered){

        doingCall = false;

        if(wasCallAnwsered){
            // do sth
        }
        else{
            // do sth else
        }

        if(lastCalledContact.getCallCount() == 0){ // CHANGE to == 0 LATER
            Form newForm = new Form(lastCalledContact.getContactId(),
                    new com.besttime.workhorse.Context(lastCalledContact, new CurrentTime()));
            formManager.sendForm(newForm);
            formManager.addNewFormToSentForms(newForm);
            json.serialize(FormManager.JSON_NAME, formManager);
        }

        lastCalledContact.addCallCount();
        json.serialize(lastCalledContact.getContactId() + lastCalledContact.getContactNumber(),
                lastCalledContact);


        json.serialize(App.nameToDeserialize, this);



    }

    public void sortContacts(List<ContactEntry> contacts){


    }

    public Date getLastLaunch() {
        return lastLaunch;

    }

    public void setLastLaunch(Date lastLaunch) {
        this.lastLaunch = lastLaunch;

    }


    public void checkAllPermissions(){
        if(!checkReadContactPermission()){
            requestReadContactPermission();
        }
        else if(!checkPhoneCallPermission()){
            requestPhoneCallPermission();
        }
        else if(!checkSendSmsPermission()){
            requestSendSmsPermission();
        }
    }


    public boolean checkReadContactPermission(){
        if(androidContext.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    public boolean checkPhoneCallPermission(){
        if(androidContext.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    public boolean checkSendSmsPermission(){
        if(androidContext.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }


    public void requestReadContactPermission(){
        ((Activity)androidContext).requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public void requestPhoneCallPermission(){
        ((Activity)androidContext).requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_PHONE_CALL);
    }

    public void requestSendSmsPermission(){
        ((Activity)androidContext).requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
    }
}
