package com.besttime.app.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.besttime.models.Contact;

import java.util.ArrayList;

public class ContactImporter {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ArrayList<Contact> contacts;
    private Context context;


    public ContactImporter(Context context) {
        this.context = context;
    }

    /**
     * Checks whether app has permission to read contacts.
     * If not requests permission from user.
     * @return true if permission is granted, false if not.
     */
    public boolean checkReadContactsPermission(){

        if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ((Activity)context).requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
        return true;
    }



}
