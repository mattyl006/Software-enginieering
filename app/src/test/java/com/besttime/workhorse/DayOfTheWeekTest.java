package com.besttime.workhorse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DayOfTheWeekTest {

    private static HashMap<Hours, Integer> map;
    private static List<Hours> hoursList;

    @BeforeClass
    public static void setup(){

        map = new HashMap<>();
        hoursList  = Arrays.asList(Hours.values());
    }

    @Test
    public void getId(){
        int id = 1;
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(id);
        Assert.assertEquals(dayOfTheWeek.getId(), id);
    }

    @Test
    public void loadDayToBeUndefinedTest(){
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(1);
        for(int i = 0; i < hoursList.size(); i++){
            map.put(hoursList.get(i), 0);
        }
        Assert.assertEquals(map,dayOfTheWeek.getMap());
    }

    @Test
    public void loadWholeDayTest(){
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(1);
        dayOfTheWeek.loadWholeDay();
        for(int i = 0; i < hoursList.size(); i++){
            map.put(hoursList.get(i), 1);
        }
        Assert.assertEquals(map,dayOfTheWeek.getMap());
    }

    @Test
    public void loadHalfDayTest1(){
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(1);
        dayOfTheWeek.loadHalfDay(true);
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

        map.put(Hours.h16_00, 0);
        map.put(Hours.h16_30, 0);
        map.put(Hours.h17_00, 0);
        map.put(Hours.h17_30, 0);
        map.put(Hours.h18_00, 0);
        map.put(Hours.h18_30, 0);
        map.put(Hours.h19_00, 0);
        map.put(Hours.h19_30, 0);
        map.put(Hours.h20_00, 0);
        map.put(Hours.h20_30, 0);
        map.put(Hours.h21_00, 0);
        map.put(Hours.h21_30, 0);
        map.put(Hours.h22_00, 0);
        Assert.assertEquals(map,dayOfTheWeek.getMap());
    }

    @Test
    public void loadHalfDayTest2(){
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(1);
        dayOfTheWeek.loadHalfDay(false);
        map.put(Hours.h6_00, 0);
        map.put(Hours.h7_00, 0);
        map.put(Hours.h8_00, 0);
        map.put(Hours.h9_00, 0);
        map.put(Hours.h10_00, 0);
        map.put(Hours.h11_00, 0);
        map.put(Hours.h12_00, 0);
        map.put(Hours.h13_00, 0);
        map.put(Hours.h14_00, 0);
        map.put(Hours.h15_00, 0);

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
        Assert.assertEquals(map,dayOfTheWeek.getMap());
    }

    @Test
    public void timeToEnumTest(){
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(1);
        Assert.assertEquals(Hours.h7_00, dayOfTheWeek.timeToEnum(7,59));
        Assert.assertEquals(Hours.h8_00, dayOfTheWeek.timeToEnum(8,0));
    }

    @Test
    public void loadOneTimeTest(){
        DayOfTheWeek dayOfTheWeek = new DayOfTheWeek(1);
        int hour = 15;
        int min = 46;
        Enum myHour = dayOfTheWeek.timeToEnum(hour,min);
        dayOfTheWeek.loadOneTime(hour,min);

        map.put(Hours.h6_00, 0);
        map.put(Hours.h7_00, 0);
        map.put(Hours.h8_00, 0);
        map.put(Hours.h9_00, 0);
        map.put(Hours.h10_00, 0);
        map.put(Hours.h11_00, 0);
        map.put(Hours.h12_00, 0);
        map.put(Hours.h13_00, 0);
        map.put(Hours.h14_00, 0);
        map.put((Hours) myHour, 1);

        map.put(Hours.h16_00, 0);
        map.put(Hours.h16_30, 0);
        map.put(Hours.h17_00, 0);
        map.put(Hours.h17_30, 0);
        map.put(Hours.h18_00, 0);
        map.put(Hours.h18_30, 0);
        map.put(Hours.h19_00, 0);
        map.put(Hours.h19_30, 0);
        map.put(Hours.h20_00, 0);
        map.put(Hours.h20_30, 0);
        map.put(Hours.h21_00, 0);
        map.put(Hours.h21_30, 0);
        map.put(Hours.h22_00, 0);

        Assert.assertEquals(map,dayOfTheWeek.getMap());
    }


}