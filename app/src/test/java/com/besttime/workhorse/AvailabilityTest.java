package com.besttime.workhorse;

import com.besttime.app.ContactEntry;
import com.besttime.models.Contact;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.besttime.workhorse.AvailType.undefined;
import static org.junit.Assert.*;

public class AvailabilityTest {


    private CurrentTime currentTime = new CurrentTime();
    private Contact testContact = new Contact(1, "John", "123456789");
    private ContactEntry testContactEntry = new ContactEntry(testContact);
    private static Map<Hours,AvailType> currentDayTest;
    private static List<Map<Hours,AvailType>> undefinedList;
    private Map<Hours,AvailType> specialDay;


    @BeforeClass
    public static void setUp(){
        undefinedList = new ArrayList<Map<Hours,AvailType>>(7);
        currentDayTest = new HashMap<Hours, AvailType>();
        Map<Hours,AvailType> map0 = new HashMap<>();
        Map<Hours,AvailType> map1 = new HashMap<>();
        Map<Hours,AvailType> map2 = new HashMap<>();
        Map<Hours,AvailType> map3 = new HashMap<>();
        Map<Hours,AvailType> map4 = new HashMap<>();
        Map<Hours,AvailType> map5 = new HashMap<>();
        Map<Hours,AvailType> map6 = new HashMap<>();

        for(Hours hour: Hours.values()){
            currentDayTest.put(hour,undefined);
            map0.put(hour,undefined);
            map1.put(hour,undefined);
            map2.put(hour,undefined);
            map3.put(hour,undefined);
            map4.put(hour,undefined);
            map5.put(hour,undefined);
            map6.put(hour,undefined);

        }

        undefinedList.add(map0);
        undefinedList.add(map1);
        undefinedList.add(map2);
        undefinedList.add(map3);
        undefinedList.add(map4);
        undefinedList.add(map5);
        undefinedList.add(map6);
    }

    @Test
    public void setUndefinedListTest_and_getAvailabilityTest()
    {

        Availability availabilityTest = new Availability(testContactEntry);
        Assert.assertEquals(undefinedList,availabilityTest.getAvailability());

    }

    @Test
    public void getCurrentDayTest(){
        Availability availabilityTest = new Availability(testContactEntry);
        Assert.assertEquals(currentDayTest,availabilityTest.getCurrentDay());
    }
    /*
    @Before
    public void addSpecialDay()
    {
        specialDay = new HashMap<>();

    }

    @Test
    public void swapdaysTest(){
        Availability availabilityTest = new Availability(testContactEntry);
        Assert.assertEquals();


    }

     */
}