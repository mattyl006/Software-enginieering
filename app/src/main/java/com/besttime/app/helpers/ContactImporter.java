package com.besttime.app.helpers;

import android.content.Context;

import com.besttime.models.Contact;

import java.util.ArrayList;

public class ContactImporter {

    private ArrayList<Contact> contacts;
    private Context context;

    public ContactImporter(Context context) {
        this.context = context;
    }
}
