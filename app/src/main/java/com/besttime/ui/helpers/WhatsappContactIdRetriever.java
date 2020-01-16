package com.besttime.ui.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class WhatsappContactIdRetriever {

    String mimeStringWhatsappVoiceCall = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
    String mimeStringWhatsappVideoCall = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";

    private ContentResolver contentResolver;


    public WhatsappContactIdRetriever(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
    
    public long getWhatsappVideoCallIdForContact(long contactId){

        long whatsappId = -1;

        String projection[] = {
                ContactsContract.Data._ID,
                ContactsContract.Data.MIMETYPE
        };

        String selection = ContactsContract.Data.CONTACT_ID + "=" + contactId;

        Cursor cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection, selection, null, null);


        int mimeTypeColInd = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        String mimeType;


        while (cursor.moveToNext()) {
            mimeType = cursor.getString(mimeTypeColInd);
            if(mimeType.equals(mimeStringWhatsappVideoCall)){
                whatsappId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
            }
        }

        return  whatsappId;
    }



    public long getWhatsappVoiceCallIdForContact(long contactId){

        long whatsappId = -1;

        String projection[] = {
                ContactsContract.Data._ID,
                ContactsContract.Data.MIMETYPE
        };

        String selection = ContactsContract.Data.CONTACT_ID + "=" + contactId;

        Cursor cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection, selection, null, null);

        //Now read data from cursor like


        int mimeTypeColInd = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        String mimeType;


        while (cursor.moveToNext()) {
            mimeType = cursor.getString(mimeTypeColInd);
            if(mimeType.equals(mimeStringWhatsappVoiceCall)){
                whatsappId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
            }
        }
        return  whatsappId;
    }
}
