package com.besttime.workhorse;

import java.util.Date;
import java.util.List;

public class Form {

    private Context context;
    private long id;
    private boolean hasResponded;
    private Date dateWhenResponded;
    private List<DayOfTheWeek> allDaysAnswers;

    public Form(long id, Context context) {
        this.context = context;
        this.id = id;
        initializeEmptyAnswers();
    }

    private void initializeEmptyAnswers(){
        for(int i = 0; i < 7; i ++){
            DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(i);
            allDaysAnswers.add(dayOfTheWeek);
        }
    }
}
