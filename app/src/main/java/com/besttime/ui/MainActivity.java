package com.besttime.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.besttime.models.Contact;
import com.besttime.ui.adapters.ContactsRecyclerViewAdapter;
import com.besttime.ui.itemsSelecting.ContactItemDetailsLookup;
import com.example.besttime.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;
    private ArrayList<Contact> contactsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar actionBar = findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);


        contactsList = new ArrayList<>();


        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(layoutManager);

        ContactsRecyclerViewAdapter contactsAdapter = new ContactsRecyclerViewAdapter(contactsList);
        contactsRecyclerView.setAdapter(contactsAdapter);

        // Create selection tracker

        SelectionTracker selectionTracker =new SelectionTracker.Builder<Long>(
                "selection-id",
                contactsRecyclerView,
                new StableIdKeyProvider(contactsRecyclerView),
                new ContactItemDetailsLookup(contactsRecyclerView),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(new SelectionTracker.SelectionPredicate<Long>() {
                    @Override
                    public boolean canSetStateForKey(@NonNull Long key, boolean nextState) {
                        return true;
                    }

                    @Override
                    public boolean canSetStateAtPosition(int position, boolean nextState) {
                        return true;
                    }

                    @Override
                    public boolean canSelectMultiple() {
                        return false;
                    }
                })
                .build();

        contactsAdapter.setSelectionTracker(selectionTracker);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
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
