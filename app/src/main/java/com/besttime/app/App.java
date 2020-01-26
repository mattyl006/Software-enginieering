package com.besttime.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.besttime.app.helpers.ContactImporter;
import com.besttime.app.helpers.WhatsappRedirector;
import com.besttime.ui.helpers.WhatsappContactIdRetriever;
import com.besttime.workhorse.FormManager;
import com.besttime.workhorse.SmsManager;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App implements Serializable {

    public final static String nameToDeserialize = "app";

    public static final int PERMISSIONS_REQUEST_SEND_SMS = 121;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 199;
    public static final int PERMISSIONS_PHONE_CALL = 197;


    private boolean firstUse;
    private List<String> contactListNames;
    private Date lastLaunch;
    private FormManager formManager = null;

    private transient Context androidContext;

    private transient ContactImporter contactImporter;
    private transient WhatsappRedirector whatsappRedirector;
    private transient WhatsappContactIdRetriever whatsappContactIdRetriever;


    /**
     *
     * @param androidContext
     * @throws IOException
     * @throws GeneralSecurityException When there was error creating formManager
     */
    public App(final Context androidContext) throws IOException, GeneralSecurityException {
        this.androidContext = androidContext;
        contactListNames = new ArrayList<>();

        setAllTransientFields(androidContext);
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


    public void setAllTransientFields(Context androidContext) throws IOException, GeneralSecurityException {
        this.androidContext = androidContext;
        contactImporter = new ContactImporter(PERMISSIONS_REQUEST_READ_CONTACTS, androidContext);
        whatsappContactIdRetriever =new WhatsappContactIdRetriever(androidContext.getContentResolver());
        whatsappRedirector = new WhatsappRedirector(androidContext, whatsappContactIdRetriever);
        formManager = new FormManager(new SmsManager(androidContext, PERMISSIONS_REQUEST_SEND_SMS));

    }

    public void appLoop(){

    }

    public void updateContactList(){

    }

    public List<ContactEntry> getContactList(){

        return null;
    }

    public void whatsappForward(ContactEntry contact){


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
