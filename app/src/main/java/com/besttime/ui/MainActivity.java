package com.besttime.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.besttime.app.ContactEntry;
import com.besttime.app.helpers.ContactImporter;
import com.besttime.app.helpers.WhatsappRedirector;
import com.besttime.app.mockClasses.MockApp;
import com.besttime.models.Contact;
import com.besttime.ui.adapters.ContactsRecyclerViewAdapter;
import com.besttime.ui.animation.ContactSelectAnimationManager;
import com.besttime.ui.helpers.WhatsappContactIdRetriever;
import com.besttime.ui.itemsSelecting.ContactItemDetailsLookup;
import com.besttime.ui.listeners.OnSwipeTouchListener;
import com.besttime.ui.viewModels.ContactEntryWithWhatsappId;
import com.example.besttime.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;
    private ContactsRecyclerViewAdapter contactsAdapter;
    private ArrayList<ContactEntry> contactsList = new ArrayList<>();

    private RelativeLayout staticSidebar;
    private static final int numOfTimeSquaresOnStaticSidebar = 18;

    private RelativeLayout movingSidebar;
    private boolean isSidebarOpened = false;
    private static final int numOfTimeRectanglesOnMovingSidebar = 36;


    private View shadowMakerAndClickBlocker;
    private final float shadowValue = 0.5f;

    private static final int partOfScreenForSidebars = 11;
    private static final int widthChangeAfterOpeningSidebar = 4;

    private int screenWidth;

    private ValueAnimator sidebarOpeningWidthAnimation;
    private ValueAnimator sidebarOpeningAlphaAnimation;
    private ValueAnimator shadowCastingAnimation;
    private long sidebarOpeningAnimationDuration = 500;


    private View movingContactItem;

    private Toolbar actionBar;


    // TEMPORARILY ADDED
    private ContactImporter contactImporter;


    private MockApp mockApp;
    private WhatsappContactIdRetriever mockWhatsappContactIdRetriever;
    private WhatsappRedirector mockWhatsappRedirector;


    private View backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intitializeActionBar();

        initializeData();

        initializeDisplayMetrics();

        shadowMakerAndClickBlocker = findViewById(R.id.shadowMakerAndClickBlocker);

        initializeContactsRecyclerView();

        initializeStaticSidebar();

        initializeMovingSidebar();

        initializeMovingContactItem();

        initializeMockApp();

        initializeBackgroundImage();

    }

    private void initializeBackgroundImage() {
        backgroundImage = findViewById(R.id.background_image);
        contactsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                backgroundImage.setY(backgroundImage.getY() - dy);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MockApp.PERMISSIONS_PHONE_CALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                }
                else {
                    // permission denied, boo!
                    Toast.makeText(this, "Phone call permission is necessary", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void initializeMockApp() {
        mockWhatsappContactIdRetriever = new WhatsappContactIdRetriever(this.getContentResolver());
        mockWhatsappRedirector = new WhatsappRedirector(this, mockWhatsappContactIdRetriever);
        mockApp = new MockApp(mockWhatsappRedirector, this);

        contactsAdapter.setWhatsappCallPerformable(mockApp);
    }

    private void initializeData() {
        contactImporter = new ContactImporter(this);
        //initializeSampleDataAndAddItToContactsList();
        getDataFromContactsImporter();
    }

    private void initializeDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    private void intitializeActionBar() {
        actionBar = findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
    }

    private void initializeContactsRecyclerView() {
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(layoutManager);

        WhatsappContactIdRetriever whatsappContactIdRetriever = new WhatsappContactIdRetriever(this.getContentResolver());
        ArrayList<ContactEntryWithWhatsappId> contactEntriesWithWhatsappId = new ArrayList<>();
        for (ContactEntry contactEntry :
                contactsList) {
            ContactEntryWithWhatsappId contactEntryWithWhatsappId = new ContactEntryWithWhatsappId(contactEntry, whatsappContactIdRetriever);
            if(contactEntryWithWhatsappId.getWhatsappVideCallId() >= 0){
                contactEntriesWithWhatsappId.add(contactEntryWithWhatsappId);
            }
        }
        contactsAdapter = new ContactsRecyclerViewAdapter(contactEntriesWithWhatsappId, null);
        contactsRecyclerView.setAdapter(contactsAdapter);
        SelectionTracker selectionTracker = buildSelectionTracker();

        contactsAdapter.setSelectionTracker(selectionTracker);
    }

    private SelectionTracker buildSelectionTracker() {
        // Create selection tracker

        return new SelectionTracker.Builder<Long>(
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
    }

    private void initializeMovingContactItem() {
        movingContactItem = findViewById(R.id.movingContactItem);

        actionBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                contactsAdapter.setAnimationManager(new ContactSelectAnimationManager(movingContactItem, shadowMakerAndClickBlocker, 500, actionBar.getHeight()));

                actionBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initializeMovingSidebar() {
        movingSidebar = findViewById(R.id.movingSidebar);
        movingSidebar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RelativeLayout.LayoutParams sidebarLayoutParams = (RelativeLayout.LayoutParams) movingSidebar.getLayoutParams();
                sidebarLayoutParams.width = screenWidth / partOfScreenForSidebars;
                movingSidebar.setLayoutParams(sidebarLayoutParams);

                int timeRectanglesHeight = (movingSidebar.getHeight() - actionBar.getHeight()) / numOfTimeRectanglesOnMovingSidebar;
                for(int i = 0; i < numOfTimeRectanglesOnMovingSidebar + 1; i ++){
                    View childOfSidebar = movingSidebar.getChildAt(i);
                    if(childOfSidebar.getId() != R.id.contactNameTextView_movingSidebar){
                        RelativeLayout.LayoutParams timeRectangleLayoutParams = (RelativeLayout.LayoutParams) childOfSidebar.getLayoutParams();
                        timeRectangleLayoutParams.height = timeRectanglesHeight;
                        childOfSidebar.setLayoutParams(timeRectangleLayoutParams);
                        childOfSidebar.setBackgroundResource(R.drawable.time_available_drawable);
                    }
                }

                int heightOfLastTimeRectangle = (movingSidebar.getHeight() - actionBar.getHeight()) - (timeRectanglesHeight * (numOfTimeRectanglesOnMovingSidebar - 1));
                TextView lastTimeRectangle = movingSidebar.findViewById(R.id.timeRectangle_23_30_movingSideBar);
                RelativeLayout.LayoutParams lastTimeRectangleLayoutParams = (RelativeLayout.LayoutParams) lastTimeRectangle.getLayoutParams();
                lastTimeRectangleLayoutParams.height = heightOfLastTimeRectangle;
                lastTimeRectangle.setLayoutParams(lastTimeRectangleLayoutParams);

                sidebarOpeningWidthAnimation = ValueAnimator.ofInt(movingSidebar.getWidth(), movingSidebar.getWidth() * widthChangeAfterOpeningSidebar);
                sidebarOpeningWidthAnimation.setDuration(sidebarOpeningAnimationDuration);
                sidebarOpeningWidthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int animatedValue = (int) valueAnimator.getAnimatedValue();

                        RelativeLayout.LayoutParams movingSidebarLayoutParams = (RelativeLayout.LayoutParams) movingSidebar.getLayoutParams();
                        movingSidebarLayoutParams.width = animatedValue;
                        movingSidebar.setLayoutParams(movingSidebarLayoutParams);

                    }
                });

                sidebarOpeningAlphaAnimation = ObjectAnimator.ofFloat(movingSidebar, "alpha", 0f, 1f);
                sidebarOpeningAlphaAnimation.setDuration(sidebarOpeningAnimationDuration);


                movingSidebar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        shadowCastingAnimation = ObjectAnimator.ofFloat(shadowMakerAndClickBlocker, "alpha", 0f, shadowValue);
        shadowCastingAnimation.setDuration(sidebarOpeningAnimationDuration);


        movingSidebar.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return super.onTouch(v, event);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                if(!isSidebarOpened){

                    sidebarOpeningWidthAnimation.start();
                    sidebarOpeningAlphaAnimation.start();
                    shadowCastingAnimation.start();

                    shadowMakerAndClickBlocker.setClickable(true);
                    shadowMakerAndClickBlocker.setFocusable(true);

                    isSidebarOpened = true;
                }

            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                if(isSidebarOpened){

                    sidebarOpeningWidthAnimation.reverse();
                    sidebarOpeningAlphaAnimation.reverse();
                    shadowCastingAnimation.reverse();

                    shadowMakerAndClickBlocker.setClickable(false);
                    shadowMakerAndClickBlocker.setFocusable(false);

                    isSidebarOpened = false;
                }

            }
        });
    }




    private void initializeStaticSidebar() {
        staticSidebar = findViewById(R.id.static_sidebar);

        staticSidebar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RelativeLayout.LayoutParams sidebarLayoutParams = (RelativeLayout.LayoutParams) staticSidebar.getLayoutParams();
                sidebarLayoutParams.width = screenWidth / partOfScreenForSidebars;
                staticSidebar.setLayoutParams(sidebarLayoutParams);


                int timeSquaresHeight = staticSidebar.getHeight() / numOfTimeSquaresOnStaticSidebar;
                for(int i = 0; i < numOfTimeSquaresOnStaticSidebar; i ++){
                    TextView timeSquare = (TextView) staticSidebar.getChildAt(i);
                    RelativeLayout.LayoutParams timeSquareLayoutParams = (RelativeLayout.LayoutParams) timeSquare.getLayoutParams();
                    timeSquareLayoutParams.height = timeSquaresHeight;
                    timeSquare.setLayoutParams(timeSquareLayoutParams);
                    timeSquare.setBackgroundResource(R.drawable.time_available_drawable);
                    timeSquare.setGravity(Gravity.CENTER);
                }

                int heightOfLastTimeSquare = staticSidebar.getHeight() - (timeSquaresHeight * (numOfTimeSquaresOnStaticSidebar - 1));
                TextView lastTimeSquare = staticSidebar.findViewById(R.id.timeSquare_textView_23_00);
                RelativeLayout.LayoutParams lastTimeSquareLayoutParams = (RelativeLayout.LayoutParams) lastTimeSquare.getLayoutParams();
                lastTimeSquareLayoutParams.height = heightOfLastTimeSquare;
                lastTimeSquare.setLayoutParams(lastTimeSquareLayoutParams);

                staticSidebar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    public void getDataFromContactsImporter(){
        ArrayList<Contact> contacts = contactImporter.getAllContacts();
        if(contacts != null){
            for (Contact contact :
                    contacts) {
                contactsList.add(new ContactEntry(contact));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchButton);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((ContactsRecyclerViewAdapter) contactsRecyclerView.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
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
            contactsList.add(new ContactEntry(sampleContact));
        }

    }
}
