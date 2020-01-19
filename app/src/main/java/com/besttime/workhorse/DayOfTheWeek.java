package com.besttime.workhorse;

import java.util.*;

public class DayOfTheWeek {

    private int id; //0 - niedziela, 1 - pn...

    private HashMap <Hours, Integer> map;
    private List<Hours> hoursList;

    public DayOfTheWeek(int id){
        this.id = id;
        map  = new HashMap<>();
        this.hoursList = Arrays.asList(Hours.values());
        loadDayToBeUndefined();
    }

    public void loadDayToBeUndefined(){     //loads whole day as undefined
        for(int i = 0; i < hoursList.size(); i++){
            map.put(hoursList.get(i), 0);
        }
    }

    public void loadWholeDay(){     //makes whole day available
        for(int i = 0; i < hoursList.size(); i++){
            map.put(hoursList.get(i), 1);
        }
    }

    public void loadHalfDay(boolean answer){ //makes half day available
        if(answer){ //6-16
            map.put(Hours.h6_00, 1);
            map.put(Hours.h7_00, 1);
            map.put(Hours.h8_00, 1);
            map.put(Hours.h9_00, 1);
            map.put(Hours.h10_00, 1);
            map.put(Hours.h11_00, 1);
            map.put(Hours.h12_00, 1);
            map.put(Hours.h13_00, 1);
            map.put(Hours.h14_00, 1);
            map.put(Hours.h15_00, 1);

        }
        else {
            map.put(Hours.h16_00, 1);
            map.put(Hours.h16_30, 1);
            map.put(Hours.h17_00, 1);
            map.put(Hours.h17_30, 1);
            map.put(Hours.h18_00, 1);
            map.put(Hours.h18_30, 1);
            map.put(Hours.h19_00, 1);
            map.put(Hours.h19_30, 1);
            map.put(Hours.h20_00, 1);
            map.put(Hours.h20_30, 1);
            map.put(Hours.h21_00, 1);
            map.put(Hours.h21_30, 1);
            map.put(Hours.h22_00, 1);
        }
    }

    public void loadOneTime(int hour, int min){
        Enum myHour = timeToEnum(hour,min);
        map.put((Hours) myHour, 1);
    }
    public Enum timeToEnum(int hour, int min){

        switch (hour){
            case 6:
                return Hours.h6_00;
            case 7:
                return Hours.h7_00;
            case 8:
                return Hours.h8_00;
            case 9:
                return Hours.h9_00;
            case 10:
                return Hours.h10_00;
            case 11:
                return Hours.h11_00;
            case 12:
                return Hours.h12_00;
            case 13:
                return Hours.h13_00;
            case 14:
                return Hours.h14_00;
            case 15:
                return Hours.h15_00;
            case 16:
                if (min < 30) {
                    return Hours.h16_00;
                }
                return Hours.h16_30;
            case 17:
                if (min < 30) {
                    return Hours.h17_00;
                }
                return Hours.h17_00;
            case 18:
                if (min < 30) {
                    return Hours.h18_00;
                }return Hours.h18_30;
            case 19:
                if (min < 30) {
                    return Hours.h19_00;
                }return Hours.h19_30;
            case 20:
                if (min < 30) {
                    return Hours.h20_00;
                }return Hours.h20_30;
            case 21:
                if (min < 30) {
                    return Hours.h21_00;
                }return Hours.h21_30;
            case 22:
                return Hours.h22_00;
        }
        return null;
    }


    public HashMap<Hours, Integer> getMap() {
        return map;
    }

    public int getId() {
        return id;
    }

/*
    public static void main(String[] args) {

        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(0);
        System.out.println(dayOfTheWeek.getMap());
        dayOfTheWeek.loadHalfDay(false);
        dayOfTheWeek.loadOneTime(16,30);
        System.out.println(dayOfTheWeek.getMap());

    }
    */
}
