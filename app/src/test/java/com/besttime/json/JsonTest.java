package com.besttime.json;

import com.besttime.app.ContactEntry;
import com.besttime.models.Contact;
import com.besttime.workhorse.AvailType;
import com.besttime.workhorse.FormManager;
import com.besttime.workhorse.Hours;
import com.besttime.workhorse.SmsManager;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonTest {


    private Json json = new Json();

    @Test
    public void serializeAndDeserializeContactEntry() {
        ContactEntry expectedContactEntry = new ContactEntry(new Contact(22, "John", "123456789"));

        String jsonName = expectedContactEntry.getClass().toString() + expectedContactEntry.getContactId();

        json.serialize(jsonName, expectedContactEntry);

        ContactEntry deserializedContactEntry = (ContactEntry) json.deserialize(jsonName);

        assertEquals(expectedContactEntry.getContactId(), deserializedContactEntry.getContactId());
        assertEquals(expectedContactEntry.getContactName(), deserializedContactEntry.getContactName());
        assertEquals(expectedContactEntry.getContactNumber(), deserializedContactEntry.getContactNumber());
        assertEquals(expectedContactEntry.getCallCount(), deserializedContactEntry.getCallCount());


        List<Map<Hours, AvailType>> expectedAvailabilityList = expectedContactEntry.getAvailability().getAvailability();
        List<Map<Hours, AvailType>> actualAvailabilityList = deserializedContactEntry.getAvailability().getAvailability();

        for(int i = 0; i < expectedAvailabilityList.size(); i ++){
            Map<Hours, AvailType> expectedAvailability = expectedAvailabilityList.get(i);
            Map<Hours, AvailType> actualAvailability = expectedAvailabilityList.get(i);

            for (Hours hour :
                    expectedAvailability.keySet()) {
                AvailType expectedAvailType = expectedAvailability.get(hour);
                AvailType actualAvailType = actualAvailability.get(hour);

                assertEquals(expectedAvailType, actualAvailType);
            }

        }
    }

}