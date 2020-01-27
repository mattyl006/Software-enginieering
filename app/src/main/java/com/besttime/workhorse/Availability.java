package com.besttime.workhorse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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


    public AvailType getCurrentAvailability(){

        Date currentTime = new CurrentTime().getTime();

        int currentHour = currentTime.getHours();
        int currentMins = currentTime.getMinutes();

        Hours currentHourEnum = DayOfTheWeek.timeToEnum(currentHour, currentMins);

        if(currentHourEnum == null){
            return AvailType.unavailable;
        }


        AvailType currentAvailability = getCurrentDay().get(currentHourEnum);
        return currentAvailability;

    }


    /**
     * This method should be called after getting answer for Query of QueriesType.query5
     * @param hourToUpdate
     * @param isAvailable 0 - not, 1 - yes
     * @param dayId 0 - Monday, 1 - Tuesday, ... , 6 - Sunday
     */
    public void updateOneHour(Hours hourToUpdate, Integer isAvailable, int dayId){

        if(hourToUpdate != null){
            AvailType currentAvailability = availability.get(dayId).get(hourToUpdate);

            if(currentAvailability.compareTo(AvailType.available) != 0){
                if(isAvailable == 0){
                    availability.get(dayId).put(hourToUpdate, AvailType.unavailable);
                }
                else if(isAvailable == 1){
                    availability.get(dayId).put(hourToUpdate, AvailType.perhaps);
                }
            }
        }

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
