package com.besttime.workhorse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuerySmsComputationTest {

    private static Week smsWeek;
    private static Week queryWeek;
    private static List<Hours> hoursList;

    @BeforeClass
    public static void beforeClass(){
        smsWeek = new Week();
        queryWeek = new Week();
        hoursList = Arrays.asList(Hours.values());
    }

    @Before
    public void before(){
        DayOfTheWeek dayOfTheWeek0 = new DayOfTheWeek(0);
        DayOfTheWeek dayOfTheWeek1 = new DayOfTheWeek(1);
        DayOfTheWeek dayOfTheWeek2 = new DayOfTheWeek(2);
        dayOfTheWeek0.loadOneTime(15,10);
        dayOfTheWeek1.loadOneTime(8,10);
        dayOfTheWeek2.loadOneTime(19,10);

        smsWeek.updateDay(dayOfTheWeek0);
        smsWeek.updateDay(dayOfTheWeek1);
        smsWeek.updateDay(dayOfTheWeek2);

        DayOfTheWeek dayOfTheWeek3 = new DayOfTheWeek(3);
        DayOfTheWeek dayOfTheWeek4 = new DayOfTheWeek(4);
        dayOfTheWeek3.loadHalfDay(true);
        dayOfTheWeek3.loadWholeDay();
        queryWeek.updateDay(dayOfTheWeek3);
        queryWeek.updateDay(dayOfTheWeek4);
    }

    @Test
    public void compareDaysTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Assert.assertEquals(AvailType.available, querySmsComputation.compareDays(1,0));
        Assert.assertEquals(AvailType.available, querySmsComputation.compareDays(1,1));
        Assert.assertEquals(AvailType.perhaps, querySmsComputation.compareDays(0,1));
        Assert.assertEquals(AvailType.unavailable, querySmsComputation.compareDays(0,0));
    }

    public Enum addDays(int sms,int query){
        if(sms > 0){
            return AvailType.available;
        }else {
            if (query == 0){
                return AvailType.unavailable;
            }
            else return AvailType.perhaps;
        }
    }

    @Test
    public void getMondayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getMonday();
        Map qM = queryWeek.getMonday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }

        Assert.assertEquals(querySmsComputation.getMonday(), mMap);
    }

    @Test
    public void getTuesdayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getTuesday();
        Map qM = queryWeek.getTuesday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        Assert.assertEquals(querySmsComputation.getTuesday(), mMap);
    }

    @Test
    public void getWednesdayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getWednesday();
        Map qM = queryWeek.getWednesday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }

        Assert.assertEquals(querySmsComputation.getWednesday(), mMap);
    }

    @Test
    public void getThursdayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getThursday();
        Map qM = queryWeek.getThursday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }

        Assert.assertEquals(querySmsComputation.getThursday(), mMap);
    }

    @Test
    public void getFridayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getFriday();
        Map qM = queryWeek.getFriday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }

        Assert.assertEquals(querySmsComputation.getFriday(), mMap);
    }

    @Test
    public void getSaturdayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getSaturday();
        Map qM = queryWeek.getSaturday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }

        Assert.assertEquals(querySmsComputation.getSaturday(), mMap);
    }

    @Test
    public void getSundayTest(){
        QuerySmsComputation querySmsComputation = new QuerySmsComputation(smsWeek, queryWeek);
        Map sM = smsWeek.getSunday();
        Map qM = queryWeek.getSunday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) addDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }

        Assert.assertEquals(querySmsComputation.getSunday(), mMap);
    }


}