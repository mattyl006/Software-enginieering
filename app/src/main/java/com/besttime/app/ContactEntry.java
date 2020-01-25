package com.besttime.app;

import com.besttime.models.Contact;
import com.besttime.workhorse.Availability;

public class ContactEntry {

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
}
