package com.besttime.workhorse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.besttime.app.ContactEntry;

public class Availability implements Serializable {

    private ContactEntry contact;
    private Map<Hours, AvailType> currentDay = new HashMap<Hours, AvailType>();
    /**
     * Map at index:
     * - 0 is Monday
     * - 1 is Tuesday
     * ...
     * - 6 is Sunday
     */
    private List<Map<Hours, AvailType>> availability = new ArrayList<Map<Hours, AvailType>>();



    public void setCurrentDay(Map<Hours, AvailType> currentDay) {
        this.currentDay = currentDay;
    }

    public Availability(ContactEntry contact){
        this.contact = contact;
        this.currentDay = new HashMap<Hours, AvailType>();
        this.currentDay = fillUndefined();
        this.availability = new ArrayList<Map<Hours,AvailType>>(7);
        setUndefindAvailability();
    }


    public void swapCurrentDay(CurrentTime currentTime) {
        int dayAsDec = currentTime.getDayOfWeekAsDecimal();
        setCurrentDay(availability.get(dayAsDec));

    }


    public List<Map<Hours, AvailType>> getAvailability() {
        return availability;
    }

    public Map<Hours, AvailType> getCurrentDay() {
        return currentDay;
    }


    public void updateAvailabilityListByForm(Form form){


    }

    private Map<Hours,AvailType> fillUndefined(){
        Map<Hours,AvailType> oneDayMap = new HashMap<>();
        for(Hours hour: Hours.values())
        {
            oneDayMap.put(hour, AvailType.undefined);
        }

        return oneDayMap;
    }

    private void setUndefindAvailability(){
        List<Map<Hours,AvailType>> tmp = new ArrayList<>(7);
        this.availability.clear();
        for(int i =0; i<7; i++){
            tmp.add(fillUndefined());
        }
        setAvailability(tmp);
    }



    public void setAvailability(List<Map<Hours, AvailType>> availability) {
        this.availability = availability;
    }



}
