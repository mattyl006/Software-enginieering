package com.besttime.workhorse.helpers;

import com.besttime.app.ContactEntry;

import java.util.Comparator;

public class ContactEntryComparator implements Comparator<ContactEntry> {

    private ContactEntryCurrentAvailabilityComparator availabilityComparator;
    private ContactEntryNameComparator nameComparator;


    public ContactEntryComparator() {
        availabilityComparator = new ContactEntryCurrentAvailabilityComparator();
        nameComparator = new ContactEntryNameComparator();
    }

    @Override
    public int compare(ContactEntry o1, ContactEntry o2) {
        if(availabilityComparator.compare(o1, o2) == 0){
            return nameComparator.compare(o1, o2);

        }
        else{
            return availabilityComparator.compare(o1, o2);
        }
    }
}
