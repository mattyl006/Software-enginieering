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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.besttime.models.Contact;
import com.besttime.ui.adapters.ContactsRecyclerViewAdapter;
import com.besttime.ui.itemsSelecting.ContactItemDetailsLookup;
import com.example.besttime.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;
    private ArrayList<Contact> contactsList;

    private RelativeLayout staticSidebar;
    private static final int numOfTimeSquaresOnStaticSidebar = 18;

    private RelativeLayout movingSidebar;
    private static final int numOfTimeRectanglesOnMovingSidebar = 36;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar actionBar = findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);


        contactsList = new ArrayList<>();
        initializeSampleDataAndAddItToContactsList();

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
                        return true;
                    }
                })
                .build();

        contactsAdapter.setSelectionTracker(selectionTracker);

        staticSidebar = findViewById(R.id.static_sidebar);

        staticSidebar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int timeSquaresHeight = staticSidebar.getHeight() / numOfTimeSquaresOnStaticSidebar;
                for(int i = 0; i < numOfTimeSquaresOnStaticSidebar; i ++){
                    TextView timeSquare = (TextView) staticSidebar.getChildAt(i);
                    RelativeLayout.LayoutParams timeSquareLayoutParams = (RelativeLayout.LayoutParams) timeSquare.getLayoutParams();
                    timeSquareLayoutParams.height = timeSquaresHeight;
                    timeSquare.setLayoutParams(timeSquareLayoutParams);
                    timeSquare.setBackgroundResource(R.drawable.rectangle_gray_border);
                }
            }
        });


        movingSidebar = findViewById(R.id.movingSidebar);
        movingSidebar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int timeRectanglesHeight = (movingSidebar.getHeight() - actionBar.getHeight()) / numOfTimeRectanglesOnMovingSidebar;
                for(int i = 0; i < numOfTimeRectanglesOnMovingSidebar + 1; i ++){
                    View childOfSidebar = movingSidebar.getChildAt(i);
                    if(childOfSidebar.getId() != R.id.contactNameTextView_movingSidebar){
                        RelativeLayout.LayoutParams timeRectangleLayoutParams = (RelativeLayout.LayoutParams) childOfSidebar.getLayoutParams();
                        timeRectangleLayoutParams.height = timeRectanglesHeight;
                        childOfSidebar.setLayoutParams(timeRectangleLayoutParams);
                        childOfSidebar.setBackgroundResource(R.drawable.rectangle_gray_border);
                    }
                }
            }
        });


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
                new Contact(7, "Kot", "444555666"),
                new Contact(8, "Kuzyn", "444555666"),
                new Contact(9, "Siostra", "444555666"),
                new Contact(10, "Brat", "444555666"),
                new Contact(11, "Tata", "444555666"),
                new Contact(12, "Mama", "444555666"),
                new Contact(13, "Babcia", "444555666"),
                new Contact(14, "Anthony", "444555666")};

        if(contactsList == null){
            contactsList = new ArrayList<>();
        }
        for (Contact sampleContact: sampleData) {
            contactsList.add(sampleContact);
        }

    }
}
