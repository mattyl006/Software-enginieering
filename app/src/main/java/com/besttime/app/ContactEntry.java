package com.besttime.app;

import com.besttime.models.Contact;

public class ContactEntry {

    private Contact contactInfo;
    private int callCount;

    public ContactEntry(Contact contactInfo) {
        this.contactInfo = contactInfo;
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
}
