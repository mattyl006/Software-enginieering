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

    public Availability(ContactEntry contact) {

        this.contact = contact;

        // Fill availability list with all days and all hours in them as undefined
        for(int i = 0; i < 7; i ++){

            availability.add(new HashMap<Hours, AvailType>());
            for (Hours hour :
                    Hours.values()) {
                availability.get(i).put(hour, AvailType.undefined);
            }
        }
        swapCurrentDay(new CurrentTime());
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


}
