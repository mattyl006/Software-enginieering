package com.besttime.workhorse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Week {


    private HashMap <Hours, Integer> monday;
    private HashMap <Hours, Integer> tuesday;
    private HashMap <Hours, Integer> wednesday;
    private HashMap <Hours, Integer> thursday;
    private HashMap <Hours, Integer> friday;
    private HashMap <Hours, Integer> saturday;
    private HashMap <Hours, Integer> sunday;

    public Week(){
        this.monday = new HashMap<>();
        this.tuesday = new HashMap<>();
        this.wednesday = new HashMap<>();
        this.thursday = new HashMap<>();
        this.friday = new HashMap<>();
        this.saturday = new HashMap<>();
        this.sunday = new HashMap<>();
        preLoad();
    }

    public void preLoad(){
        List<Hours> hoursList = Arrays.asList(Hours.values());
        for(int i = 0; i < hoursList.size(); i++){
            monday.put(hoursList.get(i), 0);
            tuesday.put(hoursList.get(i), 0);
            thursday.put(hoursList.get(i), 0);
            friday.put(hoursList.get(i), 0);
            wednesday.put(hoursList.get(i), 0);
            sunday.put(hoursList.get(i), 0);
            saturday.put(hoursList.get(i), 0);
        }
    }


    public void updateDay(DayOfTheWeek dayOfTheWeek){
        int day = dayOfTheWeek.getId();
        List<Hours> hoursList = Arrays.asList(Hours.values());
        switch (day){
            case 1:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = monday.get(hoursList.get(i));
                    monday.put(hoursList.get(i), updateValue + updatedValue);
                }
                break;
            case 2:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = tuesday.get(hoursList.get(i));
                    tuesday.put(hoursList.get(i), updateValue + updatedValue);
                }break;
            case 3:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = wednesday.get(hoursList.get(i));
                    wednesday.put(hoursList.get(i), updateValue + updatedValue);
                }break;
            case 4:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = thursday.get(hoursList.get(i));
                    thursday.put(hoursList.get(i), updateValue + updatedValue);
                }break;
            case 5:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = friday.get(hoursList.get(i));
                    friday.put(hoursList.get(i), updateValue + updatedValue);
                }break;
            case 6:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = saturday.get(hoursList.get(i));
                    saturday.put(hoursList.get(i), updateValue + updatedValue);
                }break;
            case 0:
                for(int i = 0; i < hoursList.size(); i++){
                    int updateValue = dayOfTheWeek.getMap().get(hoursList.get(i));
                    int updatedValue = sunday.get(hoursList.get(i));
                    sunday.put(hoursList.get(i), updateValue + updatedValue);
                }break;
        }
    }

    public HashMap<Hours, Integer> getMonday() {
        return monday;
    }

    public HashMap<Hours, Integer> getTuesday() {
        return tuesday;
    }

    public HashMap<Hours, Integer> getWednesday() {
        return wednesday;
    }

    public HashMap<Hours, Integer> getThursday() {
        return thursday;
    }

    public HashMap<Hours, Integer> getFriday() {
        return friday;
    }

    public HashMap<Hours, Integer> getSaturday() {
        return saturday;
    }

    public HashMap<Hours, Integer> getSunday() {
        return sunday;
    }
/*
    public static void main(String[] args) {
        DayOfTheWeek day1 = new DayOfTheWeek(1);
        DayOfTheWeek day2 = new DayOfTheWeek(1);
        DayOfTheWeek day3 = new DayOfTheWeek(0);

        day1.loadOneTime(16, 30);
        day2.loadOneTime(16, 20);
        day3.loadOneTime(16, 20);

        Week week = new Week();
        week.updateDay(day1);
        week.updateDay(day2);
        week.updateDay(day3);

        System.out.println(week.getMonday());
        System.out.println(week.getSaturday());
        System.out.println(week.getSunday());
    }

     */
}
