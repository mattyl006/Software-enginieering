package com.besttime.workhorse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WeekTest {

    private static DayOfTheWeek dayOfTheWeek0;
    private static DayOfTheWeek dayOfTheWeek1;
    private static DayOfTheWeek dayOfTheWeek2;
    private static DayOfTheWeek dayOfTheWeek3;
    private static DayOfTheWeek dayOfTheWeek4;
    private static DayOfTheWeek dayOfTheWeek5;
    private static DayOfTheWeek dayOfTheWeek6;


    private HashMap<Hours, Integer> monday;
    private HashMap <Hours, Integer> tuesday;
    private HashMap <Hours, Integer> wednesday;
    private HashMap <Hours, Integer> thursday;
    private HashMap <Hours, Integer> friday;
    private HashMap <Hours, Integer> saturday;
    private HashMap <Hours, Integer> sunday;

    @BeforeClass
    public static void setup() {

        dayOfTheWeek0 = new DayOfTheWeek(0);
        dayOfTheWeek1 = new DayOfTheWeek(1);
        dayOfTheWeek2 = new DayOfTheWeek(2);
        dayOfTheWeek3 = new DayOfTheWeek(3);
        dayOfTheWeek4 = new DayOfTheWeek(4);
        dayOfTheWeek5 = new DayOfTheWeek(5);
        dayOfTheWeek6 = new DayOfTheWeek(6);

        dayOfTheWeek0.loadOneTime(15,20);
        dayOfTheWeek1.loadOneTime(15,20);
        dayOfTheWeek2.loadOneTime(15,20);
        dayOfTheWeek3.loadOneTime(15,20);
        dayOfTheWeek4.loadOneTime(15,20);
        dayOfTheWeek5.loadOneTime(15,20);
        dayOfTheWeek6.loadOneTime(15,20);
    }

    @Before
    public void loadDays(){
        monday = new HashMap<>();
        tuesday = new HashMap<>();
        wednesday = new HashMap<>();
        thursday = new HashMap<>();
        friday = new HashMap<>();
        saturday = new HashMap<>();
        sunday = new HashMap<>();
    }

    @Test
    public void preLoadTest(){

        Week week = new Week();

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

        Assert.assertEquals(sunday,week.getSunday());
        Assert.assertEquals(monday,week.getMonday());
        Assert.assertEquals(tuesday,week.getTuesday());
        Assert.assertEquals(wednesday,week.getWednesday());
        Assert.assertEquals(thursday,week.getThursday());
        Assert.assertEquals(friday,week.getFriday());
        Assert.assertEquals(saturday,week.getSunday());
    }

    @Test
    public void getTuesdayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek2);
        Assert.assertEquals(week.getTuesday(), dayOfTheWeek2.getMap());
    }

    @Test
    public void getWednesdayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek3);
        Assert.assertEquals(week.getWednesday(), dayOfTheWeek3.getMap());
    }

    @Test
    public void getThursdayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek4);
        Assert.assertEquals(week.getThursday(), dayOfTheWeek4.getMap());
    }

    @Test
    public void getFridayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek5);
        Assert.assertEquals(week.getFriday(), dayOfTheWeek5.getMap());
    }

    @Test
    public void getSaturdayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek6);
        Assert.assertEquals(week.getSaturday(), dayOfTheWeek6.getMap());
    }

    @Test
    public void getSundayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek0);
        Assert.assertEquals(week.getSunday(), dayOfTheWeek0.getMap());
    }

    @Test
    public void getMondayTest(){
        Week week = new Week();
        week.updateDay(dayOfTheWeek1);
        Assert.assertEquals(week.getMonday(), dayOfTheWeek1.getMap());
    }

    @Test
    public void updateDayTest(){
        Week week = new Week();
        Assert.assertNotEquals(dayOfTheWeek1.getMap(), week.getMonday());
        week.updateDay(dayOfTheWeek1);
        Assert.assertEquals(dayOfTheWeek1.getMap(), week.getMonday());
    }
}