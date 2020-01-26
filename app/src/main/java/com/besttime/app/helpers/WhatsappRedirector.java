package com.besttime.app.helpers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.besttime.app.ContactEntry;
import com.besttime.ui.helpers.WhatsappContactIdRetriever;

public class WhatsappRedirector {
    private final String mimeStringWhatsappVideoCall = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";

    private Context context;
    private WhatsappContactIdRetriever whatsappContactIdRetriever;

    /**
     *
     * @param context
     * @param whatsappContactIdRetriever This is needed to get video call id, which has to be retrieved dynamically, because it may change over time
     */
    public WhatsappRedirector(Context context, WhatsappContactIdRetriever whatsappContactIdRetriever) {
        this.context = context;
        this.whatsappContactIdRetriever = whatsappContactIdRetriever;
    }

    /**
     * Performs whatsapp video call for given contact entry
     * @param contactEntry
     */
    public void redirectToWhatsappVideoCall(ContactEntry contactEntry, int whatsappVideoCallRequestCode){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        long whatsappVideoCallId = whatsappContactIdRetriever.getWhatsappVideoCallIdForContact(contactEntry.getContactId());

        if(whatsappVideoCallId >= 0){
            intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + whatsappVideoCallId), mimeStringWhatsappVideoCall);
            intent.setPackage("com.whatsapp");


            ActivityOptions whatsappCallOptions = ActivityOptions.makeBasic();

            ((Activity)context).startActivityForResult(intent, whatsappVideoCallRequestCode, whatsappCallOptions.toBundle());
        }
    }


}
