package com.besttime.app.mockClasses;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.besttime.app.ContactEntry;
import com.besttime.app.helpers.WhatsappCallPerformable;
import com.besttime.app.helpers.WhatsappRedirector;

import static android.Manifest.permission.CALL_PHONE;

public class MockApp implements WhatsappCallPerformable {

    private WhatsappRedirector whatsappRedirector;
    private Context context;

    public static final int PERMISSIONS_PHONE_CALL = 110;


    public MockApp(WhatsappRedirector whatsappRedirector, Context context) {
        this.whatsappRedirector = whatsappRedirector;
        this.context = context;
    }

    @Override
    public void whatsappForward(ContactEntry contactEntry) {
        if(checkCallPermission() != true){
            requestPhoneCallPermission();
        }

        if(checkCallPermission() == true){
            whatsappRedirector.redirectToWhatsappVideoCall(contactEntry);
        }
    }


    public boolean checkCallPermission(){

        if (context.checkSelfPermission(CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void requestPhoneCallPermission(){
        ((Activity)context).requestPermissions(new String[]{CALL_PHONE},PERMISSIONS_PHONE_CALL);
    }







}
