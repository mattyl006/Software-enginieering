package com.besttime.ui.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class WhatsappContactIdRetriever {

    String mimeStringWhatsappVoiceCall = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
    String mimeStringWhatsappVideoCall = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";

    private ContentResolver contentResolver;


    /**
     * This class is used to retrieve ids used to make whatsapp video or voice calls.
     * @param contentResolver
     */
    public WhatsappContactIdRetriever(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     *
     * @param contactId
     * @return Id which can be used for making whatsapp video call or -1 if no id was found.
     */
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


    /**
     *
     * @param contactId
     * @return Id which can be used to make whatsapp voice call or -1 if no id was found.
     */
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
