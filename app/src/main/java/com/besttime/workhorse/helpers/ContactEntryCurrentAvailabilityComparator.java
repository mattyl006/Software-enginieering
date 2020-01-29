package com.besttime.workhorse.helpers;

import com.besttime.app.ContactEntry;
import com.besttime.workhorse.AvailType;

import java.util.Comparator;

public class ContactEntryCurrentAvailabilityComparator implements Comparator<ContactEntry> {
    @Override
    public int compare(ContactEntry o1, ContactEntry o2) {

        AvailType o1AvailType = o1.getAvailability().getCurrentAvailability();
        AvailType o2AvailType = o2.getAvailability().getCurrentAvailability();


        if(o1AvailType.compareTo(o2AvailType) == 0){
            return 0;
        }
        else{
            switch (o1AvailType){

                case undefined:
                    return 1;
                case available:
                    return -1;
                case unavailable:
                    switch (o2AvailType){

                        case undefined:
                            return -1;
                        case available:
                            return 1;
                        case perhaps:
                            return 1;
                    }
                    break;
                case perhaps:
                    switch (o2AvailType){

                        case unavailable:
                            return -1;
                        case available:
                            return 1;
                        case undefined:
                            return -1;
                    }
                    break;
            }
        }

        return 0;
    }
}
