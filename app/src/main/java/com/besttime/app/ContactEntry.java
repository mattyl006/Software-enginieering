package com.besttime.app;

import com.besttime.models.Contact;
import com.besttime.workhorse.Availability;

import java.io.Serializable;

public class ContactEntry implements Serializable {

    private Contact contactInfo;
    private int callCount;
    private Availability availability;

    
    public ContactEntry(Contact contactInfo) {
        this.contactInfo = contactInfo;
        availability = new Availability(this);
    }

    public int getCallCount(){
        return callCount;
    }

    public void addCallCount(){
        callCount++;
    }

    public String getContactName(){
        return contactInfo.getName();
    }

    public  String getContactNumber(){
        return contactInfo.getPhoneNumber();
    }

    public int getContactId(){ return contactInfo.getId();}

    public Availability getAvailability() {
        return availability;
    }

    public void changeContactName(String newName){
        contactInfo.changeName(newName);
    }

    public String generateNameForJson(){
        return getContactId()+ "_" + getContactNumber();
    }


    /**
     *
     * @param jsonName String in format {id}_{phoneNum}.
     * @return String[2] result:
     * - result[0] - id
     * - result[1] - phone num
     */
    public static String[] getContactInfoFromJsonName(String jsonName){

        String[] result = jsonName.split("_");

        return result;

    }
}
