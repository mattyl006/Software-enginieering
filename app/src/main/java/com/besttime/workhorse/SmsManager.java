package com.besttime.workhorse;

public class SmsManager {

    private android.telephony.SmsManager androidSmsManager;

    public SmsManager() {
        androidSmsManager = android.telephony.SmsManager.getDefault();
    }

    public void sendSmsPrompt(Context context, String message){
        androidSmsManager.sendTextMessage(context.getContact().getContactNumber(), null, message, null, null);
    }
}
