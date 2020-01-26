package com.besttime.workhorse;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

public class SmsManager {

    private android.telephony.SmsManager androidSmsManager;

    private android.content.Context androidContext;
    private final int sendSmsRequestCode;

    public SmsManager(android.content.Context androidContext, int sendSmsRequestCode) {
        this.androidContext = androidContext;
        this.sendSmsRequestCode = sendSmsRequestCode;
        androidSmsManager = android.telephony.SmsManager.getDefault();
    }

    public void sendSmsPrompt(Context context, String message){
        if(checkSendSmsPermission(androidContext, sendSmsRequestCode)){
            androidSmsManager.sendTextMessage(context.getContact().getContactNumber(), null, message, null, null);
        }
    }

    public boolean checkSendSmsPermission(android.content.Context androidContext, int sendSmsRequestCode){
        if (androidContext.checkSelfPermission(Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ((Activity)androidContext).requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    sendSmsRequestCode);
            return false;
        }
        return true;
    }

    public android.content.Context getAndroidContext() {
        return androidContext;
    }
}
