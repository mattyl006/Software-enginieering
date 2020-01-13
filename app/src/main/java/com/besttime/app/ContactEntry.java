package com.besttime.app;

import com.besttime.models.Contact;

public class ContactEntry {

    private Contact contactInfo;
    private int callCount;

    public ContactEntry(Contact contactInfo, int callCount) {
        this.contactInfo = contactInfo;
        this.callCount = callCount;
    }

    public int getCallCount(){
        return callCount;
    }

    public void addCallCount(){
        callCount++;
    }
}
