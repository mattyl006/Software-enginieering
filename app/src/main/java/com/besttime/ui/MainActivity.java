package com.besttime.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.besttime.app.ContactEntry;
import com.besttime.models.Contact;
import com.besttime.ui.adapters.ContactsRecyclerViewAdapter;
import com.besttime.workhorse.Context;
import com.besttime.workhorse.CurrentTime;
import com.besttime.workhorse.SmsManager;
import com.example.besttime.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;
    private ArrayList<Contact> contactsList;

    private static final int PERMISSIONS_REQUEST_SEND_SMS = 121;

    private SmsManager smsManager = new SmsManager(this, PERMISSIONS_REQUEST_SEND_SMS);

    private EditText smsMessageTextView;
    private EditText phoneNumberTextView;
    private Button sendSMSButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsMessageTextView = findViewById(R.id.smsMessage_textView);
        phoneNumberTextView = findViewById(R.id.phoneNumber_TextView);
        sendSMSButton = findViewById(R.id.sendSms_button);

        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = phoneNumberTextView.getText().toString();
                String message = smsMessageTextView.getText().toString();

                smsManager.sendSmsPrompt(new Context(new ContactEntry(new Contact(123, "ASD", phoneNum)), new CurrentTime()), message);
            }
        });



        smsManager.checkSendSmsPermission(this, PERMISSIONS_REQUEST_SEND_SMS);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                }
                else {
                    // permission denied, boo!
                    Toast.makeText(this, "Send SMS permission is necessary", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }

                break;
        }
    }

    private void initializeSampleDataAndAddItToContactsList() {
        Contact[] sampleData = new Contact[]{
                new Contact(1, "John", "123456789"),
                new Contact(2, "Mary", "987654321"),
                new Contact(3, "Bob", "111222333"),
                new Contact(4, "Marek", "444555666"),
                new Contact(5, "Janek", "444555666"),
                new Contact(6, "Anna", "444555666"),
                new Contact(7, "Noname", "444555666")};

        if(contactsList == null){
            contactsList = new ArrayList<>();
        }
        for (Contact sampleContact: sampleData) {
            contactsList.add(sampleContact);
        }

    }
}
