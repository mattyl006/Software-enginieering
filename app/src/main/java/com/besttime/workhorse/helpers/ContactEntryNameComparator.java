package com.besttime.workhorse.helpers;

import com.besttime.app.ContactEntry;

import java.util.Comparator;

public class ContactEntryNameComparator implements Comparator<ContactEntry> {


    @Override
    public int compare(ContactEntry o1, ContactEntry o2) {
        return o1.getContactName().compareToIgnoreCase(o2.getContactName());
    }
}
