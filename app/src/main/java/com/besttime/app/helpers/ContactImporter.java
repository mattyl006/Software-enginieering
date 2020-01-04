package com.besttime.app.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

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


    /**
     *
     * @param contactId contact id retrieved from ContactsContract.Contacts table
     * @return Array of phone numbers for given contact id
     */
    private String[] getPhoneNumbersForContact(int contactId) {

        String[] phoneNumbers;

        String[] phonesProjection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor phonesCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                phonesProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                null,
                null);

        int phoneNumberColIndex = phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        phoneNumbers = new String[phonesCursor.getCount()];

        int i = 0;
        while(phonesCursor.moveToNext()){
            phoneNumbers[i] = phonesCursor.getString(phoneNumberColIndex);
        }

        return phoneNumbers;

    }





}
