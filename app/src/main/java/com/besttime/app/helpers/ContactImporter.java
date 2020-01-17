package com.besttime.app.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.besttime.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactImporter {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ArrayList<Contact> contacts;
    private Context context;

    public boolean areImported;


    public ContactImporter(Context context) {
        this.context = context;
        areImported = false;
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
     * @return {@code ArrayList}of contacts or {@code null} if importing contacts failed
     */
    public ArrayList<Contact> getAllContacts(){
        if(areImported){
            return contacts;
        }
        else{
            if(checkReadContactsPermission()){
                importContactsFromAndroid();
            }
            else{
                return null;
            }
        }
        return contacts;
    }

    private void importContactsFromAndroid(){
        String[] contactsProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor contactsCursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                contactsProjection,
                null,
                null,
                null
        );

        int idColIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID);
        int nameColIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int hasPhoneNumberColIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        contacts = new ArrayList<>();

        while(contactsCursor.moveToNext()){
            boolean hasPhoneNumber = contactsCursor.getInt(hasPhoneNumberColIndex) > 0 ? true : false;
            if(hasPhoneNumber){
                int contactId = contactsCursor.getInt(idColIndex);
                String contactName = contactsCursor.getString(nameColIndex);

                List<String> phoneNumbers = getPhoneNumbersForContact(contactId);
                int phoneNumbersNum = phoneNumbers.size();

                if(phoneNumbersNum == 1){
                    contacts.add(new Contact(contactId, contactName, phoneNumbers.get(0)));
                }
                else{
                    for(int i = 0; i < phoneNumbersNum; i ++){
                        if(i == 0){
                            contacts.add(new Contact(contactId, contactName, phoneNumbers.get(i)));
                        }
                        else{
                            contacts.add(new Contact(contactId, String.format("%s (%d)", contactName, i+1), phoneNumbers.get(i)));
                        }
                    }
                }
            }
        }
        areImported = true;
    }


    /**
     *
     * @param contactId contact id retrieved from ContactsContract.Contacts table
     * @return Array of phone numbers for given contact id
     */
    private List<String> getPhoneNumbersForContact(int contactId) {

        List<String> phoneNumbers;

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

        phoneNumbers = new ArrayList<>();


        while(phonesCursor.moveToNext()){
            String phoneNumber = phonesCursor.getString(phoneNumberColIndex);
            // Remove whitespace
            phoneNumber = phoneNumber.replaceAll("\\s","");

            if(!phoneNumbers.contains(phoneNumber)){
                phoneNumbers.add(phoneNumber);
            }
        }

        return phoneNumbers;

    }





}
