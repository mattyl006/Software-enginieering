package com.besttime.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.app.App;
import com.besttime.app.ContactEntry;
import com.besttime.app.ContactsWithColorsContainer;
import com.besttime.json.Json;
import com.besttime.models.Contact;
import com.besttime.ui.adapters.ContactsRecyclerViewAdapter;
import com.besttime.ui.animation.ContactSelectAnimationManager;
import com.besttime.ui.helpers.WhatsappContactIdRetriever;
import com.besttime.ui.itemsSelecting.ContactItemDetailsLookup;
import com.besttime.ui.listeners.OnSwipeTouchListener;
import com.besttime.ui.utils.ContactSelectionListenable;
import com.besttime.ui.viewModels.ContactEntryWithWhatsappId;
import com.besttime.workhorse.AvailType;
import com.besttime.workhorse.CurrentTime;
import com.besttime.workhorse.Hours;
import com.example.besttime.R;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ContactSelectionListenable, ContactsWithColorsContainer {

    private RecyclerView contactsRecyclerView;
    private ContactsRecyclerViewAdapter contactsAdapter;
    private List<ContactEntry> contactsList = new ArrayList<>();

    private RelativeLayout staticSidebar;
    private static final int numOfTimeSquaresOnStaticSidebar = 17;

    private RelativeLayout movingSidebar;
    private boolean isSidebarOpened = false;
    private static final int numOfTimeRectanglesOnMovingSidebar = 23;


    private View shadowMakerAndClickBlocker;
    private final float shadowValue = 0.5f;

    private static final int partOfScreenForSidebars = 11;
    private static final int widthChangeAfterOpeningSidebar = 4;

    private int screenWidth;

    private ValueAnimator sidebarOpeningWidthAnimation;
    private ValueAnimator sidebarOpeningAlphaAnimation;
    private ValueAnimator shadowCastingAnimation;
    private long sidebarOpeningAnimationDuration = 400;


    private View movingContactItem;

    private Toolbar actionBar;

    private TextView contactNameTextView;


    private View backgroundImage;

    private App app;

    private Json json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(threadPolicy);

        intitializeActionBar();

        try {
            initializeApp();
        } catch (GeneralSecurityException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
            finishAndRemoveTask();
        }




    }

    public void initializeApp() throws GeneralSecurityException, ParseException, ClassNotFoundException {
        json = new Json(this);

        try {
            app = (App) json.deserialize(App.nameToDeserialize);

            app.setAllTransientFields(this, this);

            getAllContactsFromAppAndInitializeAllViews();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                app = new App(this, this);
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Error starting app", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            } catch (GeneralSecurityException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Error starting app", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error starting app", Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
        }
        finally {
            app.checkAllPermissions();
        }


        app.setLastLaunch(new CurrentTime().getTime());

        json.serialize(App.nameToDeserialize, app);
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.aboutWindow:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;

            case R.id.importContactsOption:
                if(app != null){
                    try {
                        app.importContacts();

                        contactsList = app.getListOfContactsCallableByWhatsapp();

                        if(contactsAdapter != null){
                            contactsAdapter.setContactsList(contactsList);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error importing contacts", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error importing contacts", Toast.LENGTH_LONG).show();
                    }

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == App.WHATSAPP_VIDEO_CALL_REQUEST){

            boolean wasCallAnwsered = false;
            if(resultCode == RESULT_OK){
                wasCallAnwsered = true;
            }
            if(resultCode == RESULT_CANCELED){
                wasCallAnwsered = false;
            }
            app.onWhatsappVideoCallEnd(wasCallAnwsered);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case App.PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    app.checkAllPermissions();
                }
                else {
                    Toast.makeText(this, "Permission is necessary", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
                break;

            case App.PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        app.importContacts();
                        json.serialize(App.nameToDeserialize, app);
                        getAllContactsFromAppAndInitializeAllViews();
                        app.checkAllPermissions();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error importing contacts", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error importing contacts", Toast.LENGTH_LONG).show();
                    }


                }
                else {
                    Toast.makeText(this, "Permission is necessary", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }

                break;


            case App.PERMISSIONS_PHONE_CALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    app.checkAllPermissions();
                }
                else {
                    Toast.makeText(this, "Permission is necessary", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
                break;


        }
    }

    private void getAllContactsFromAppAndInitializeAllViews() {

        contactsList = app.getContactList();

        initializeDisplayMetrics();

        shadowMakerAndClickBlocker = findViewById(R.id.shadowMakerAndClickBlocker);
        shadowMakerAndClickBlocker.setVisibility(View.GONE);

        shadowMakerAndClickBlocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSidebarOpened){
                    sidebarOpeningWidthAnimation.reverse();
                    sidebarOpeningAlphaAnimation.reverse();
                    shadowCastingAnimation.reverse();

                    isSidebarOpened = false;
                }
            }
        });


        initializeStaticSidebar();

        initializeMovingSidebar();

        initializeMovingContactItem();

        initializeContactsRecyclerView();

        initializeBackgroundImage();



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


        List<ContactEntry> contactEntriesWithWhatsappId = app.getListOfContactsCallableByWhatsapp();

        contactsAdapter = new ContactsRecyclerViewAdapter(contactEntriesWithWhatsappId, app, this, app);
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
                contactsAdapter.setAnimationManager(new ContactSelectAnimationManager(movingContactItem, shadowMakerAndClickBlocker, sidebarOpeningAnimationDuration, actionBar.getHeight()));

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
                TextView lastTimeRectangle = movingSidebar.findViewById(R.id.timeRectangle_22_00_movingSideBar);
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

                contactNameTextView = movingSidebar.findViewById(R.id.contactNameTextView_movingSidebar);

                movingSidebar.setVisibility(View.GONE);

                contactSelectionChanged(contactsAdapter.getSelectedContact() != null ? contactsAdapter.getSelectedContact() : null );

                movingSidebar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        shadowCastingAnimation = ObjectAnimator.ofFloat(shadowMakerAndClickBlocker, "alpha", 0f, shadowValue);
        shadowCastingAnimation.setDuration(sidebarOpeningAnimationDuration);
        shadowCastingAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                if(!isReverse){
                    shadowMakerAndClickBlocker.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if(isReverse){
                    shadowMakerAndClickBlocker.setVisibility(View.GONE);
                }
            }
        });


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
                    //timeSquare.setBackgroundResource(R.drawable.time_available_drawable); // TEST
                    timeSquare.setGravity(Gravity.CENTER);
                }

                int heightOfLastTimeSquare = staticSidebar.getHeight() - (timeSquaresHeight * (numOfTimeSquaresOnStaticSidebar - 1));
                TextView lastTimeSquare = staticSidebar.findViewById(R.id.timeSquare_textView_22_00);
                RelativeLayout.LayoutParams lastTimeSquareLayoutParams = (RelativeLayout.LayoutParams) lastTimeSquare.getLayoutParams();
                lastTimeSquareLayoutParams.height = heightOfLastTimeSquare;
                lastTimeSquare.setLayoutParams(lastTimeSquareLayoutParams);


                //staticSidebar.setVisibility(View.GONE); // TEST

                staticSidebar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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

    @Override
    public void contactSelectionChanged(@Nullable ContactEntry newSelectedContact) {
        if(newSelectedContact != null){


            movingSidebar.setVisibility(View.VISIBLE);
            staticSidebar.setVisibility(View.VISIBLE);
            changeColorsOfTimeSquaresOnSideBar(newSelectedContact);

            if(contactNameTextView != null){
                contactNameTextView.setText(newSelectedContact.getContactName());
            }

        }
        else if(newSelectedContact == null){
            if(contactNameTextView != null){
                contactNameTextView.setText("");
            }
            staticSidebar.setVisibility(View.GONE);
            movingSidebar.setVisibility(View.GONE);

        }
    }

    private void changeColorsOfTimeSquaresOnSideBar(@NonNull ContactEntry newSelectedContact) {
        newSelectedContact.getAvailability().swapCurrentDay(new CurrentTime());
        Map<Hours, AvailType> currentDay = newSelectedContact.getAvailability().getCurrentDay();

        Calendar rightNow = Calendar.getInstance();
        double currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        if(currentHour >= 16){
            double minutes = rightNow.get(Calendar.MINUTE);
            if(minutes >= 30){
                currentHour += 0.30;
            }
        }





        // Iterate over all hours in current day and set background of time squares based on avail type
        for (Hours hour:
             currentDay.keySet()) {

            View timeSquareOnStaticSidebar = null;
            View timeRectangleOnMovingSidebar = null;
            AvailType availType = null;
            switch (hour){
                case h6_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_6_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_6_00_movingSideBar);
                    availType = currentDay.get(Hours.h6_00);
                    break;
                case h7_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_7_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_7_00_movingSideBar);
                    availType = currentDay.get(Hours.h7_00);
                    break;
                case h8_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_8_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_8_00_movingSideBar);
                    availType = currentDay.get(Hours.h8_00);
                    break;
                case h9_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_9_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_9_00_movingSideBar);
                    availType = currentDay.get(Hours.h9_00);
                    break;
                case h10_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_10_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_10_00_movingSideBar);
                    availType = currentDay.get(Hours.h10_00);
                    break;
                case h11_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_11_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_11_00_movingSideBar);
                    availType = currentDay.get(Hours.h11_00);
                    break;
                case h12_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_12_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_12_00_movingSideBar);
                    availType = currentDay.get(Hours.h12_00);
                    break;
                case h13_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_13_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_13_00_movingSideBar);
                    availType = currentDay.get(Hours.h13_00);
                    break;
                case h14_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_14_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_14_00_movingSideBar);
                    availType = currentDay.get(Hours.h14_00);
                    break;
                case h15_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_15_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_15_00_movingSideBar);
                    availType = currentDay.get(Hours.h15_00);
                    break;
                case h16_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_16_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_16_00_movingSideBar);
                    availType = currentDay.get(Hours.h16_00);
                    break;
                case h16_30:
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_16_30_movingSideBar);
                    availType = currentDay.get(Hours.h16_30);
                    break;
                case h17_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_17_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_17_00_movingSideBar);
                    availType = currentDay.get(Hours.h17_00);
                    break;
                case h17_30:
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_17_30_movingSideBar);
                    availType = currentDay.get(Hours.h17_30);
                    break;
                case h18_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_18_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_18_00_movingSideBar);
                    availType = currentDay.get(Hours.h18_00);
                    break;
                case h18_30:
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_18_30_movingSideBar);
                    availType = currentDay.get(Hours.h18_30);
                    break;
                case h19_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_19_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_19_00_movingSideBar);
                    availType = currentDay.get(Hours.h19_00);
                    break;
                case h19_30:
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_19_30_movingSideBar);
                    availType = currentDay.get(Hours.h19_30);
                    break;
                case h20_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_20_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_20_00_movingSideBar);
                    availType = currentDay.get(Hours.h20_00);
                    break;
                case h20_30:
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_20_30_movingSideBar);
                    availType = currentDay.get(Hours.h20_30);
                    break;
                case h21_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_21_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_21_00_movingSideBar);
                    availType = currentDay.get(Hours.h21_00);
                    break;
                case h21_30:
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_21_30_movingSideBar);
                    availType = currentDay.get(Hours.h21_30);
                    break;
                case h22_00:
                    timeSquareOnStaticSidebar = staticSidebar.findViewById(R.id.timeSquare_textView_22_00);
                    timeRectangleOnMovingSidebar = movingSidebar.findViewById(R.id.timeRectangle_22_00_movingSideBar);
                    availType = currentDay.get(Hours.h22_00);
                    break;
            }


            if(hour.getHourValue() == currentHour){
                switch (availType){
                    case unavailable:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_unavailable_drawable_active);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_unavailable_drawable_active);
                        break;
                    case available:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_available_drawable_active);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_available_drawable_active);
                        break;
                    case undefined:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_undefined_drawable_active);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_undefined_drawable_active);
                        break;
                    case perhaps:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_perhaps_drawable_active);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_perhaps_drawable_active);
                        break;
                }
            }
            else if(hour.getHourValue() != currentHour){
                switch (availType){
                    case unavailable:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_unavailable_drawable);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_unavailable_drawable);
                        break;
                    case available:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_available_drawable);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_available_drawable);
                        break;
                    case undefined:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_undefined_drawable);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_undefined_drawable);
                        break;
                    case perhaps:
                        if(timeSquareOnStaticSidebar!= null){timeSquareOnStaticSidebar.setBackgroundResource(R.drawable.time_perhaps_drawable);}
                        timeRectangleOnMovingSidebar.setBackgroundResource(R.drawable.time_perhaps_drawable);
                        break;
                }
            }

        }
    }

    @Override
    public void updateColorsOfCurrentlySelectedContact(ContactEntry selectedContact) {
        changeColorsOfTimeSquaresOnSideBar(selectedContact);

        contactsAdapter.sortContactsAfterAvailabilityChange();
    }
}
