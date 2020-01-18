package com.besttime.workhorse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Availability {

private ContactEntry contact;
private List<Map<Hours,AvailType>> availability = new ArrayList<Map<Hours,AvailType>>();

    public void setCurrentDay(Map<Hours, AvailType> currentDay) {
        this.currentDay = currentDay;
    }

    private Map<Hours,AvailType> currentDay = new HashMap<Hours, AvailType>();


public Availability(ContactEntry contact){
    this.contact = contact;
}


    public void swapDays(CurrentTime currentTime){
    int dayAsDec = currentTime.getDayOfWeekAsDecimal();
    setCurrentDay(availability[dayAsDec]);

    }
}
