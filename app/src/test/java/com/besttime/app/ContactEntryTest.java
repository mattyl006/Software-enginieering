package com.besttime.app;

import com.besttime.models.Contact;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContactEntryTest {

    private Contact testContact = new Contact(1, "John", "123456789");



    @Test
    public void getCallCount() {
        ContactEntry testContactEntry = new ContactEntry(testContact);
        int expectedCallCount = 0;
        int callCount = testContactEntry.getCallCount();

        assertEquals(expectedCallCount, callCount);
    }

    @Test
    public void addCallCount() {
        ContactEntry testContactEntry = new ContactEntry(testContact);
        int expectedCallCount = 1;
        int callCount;

        testContactEntry.addCallCount();
        callCount = testContactEntry.getCallCount();

        assertEquals(expectedCallCount, callCount);
    }

    @Test
    public void getContactName() {
    }

    @Test
    public void getContactNumber() {
    }
}