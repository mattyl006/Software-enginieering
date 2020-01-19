package com.besttime.workhorse;


import com.besttime.app.ContactEntry;

import java.util.Date;

public class Context {

    private Date time;
    private ContactEntry contact;

    public Context(ContactEntry contact, CurrentTime currentTime) {
        this.time = currentTime.getTime();
        this.contact = contact;
    }

    public Date getTime() {
        return this.time;
    }

    public ContactEntry getContact() {
        return this.contact;
    }
}