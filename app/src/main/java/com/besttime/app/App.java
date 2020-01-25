package com.besttime.app;

import com.besttime.workhorse.FormManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class App implements Serializable {

    public final static String nameToDeserialize = "app";

    private boolean firstUse;
    private List<String> contactListNames;
    private Date lastLaunch;
    private FormManager formManager;


    public App(){


    }

    public void appLoop(){

    }

    public void updateContactList(){

    }

    public List<ContactEntry> getContactList(){

        return null;
    }

    public void whatsappForward(ContactEntry contact){


    }

    public void sortContacts(List<ContactEntry> contacts){


    }

    public Date getLastLaunch() {
        return lastLaunch;

    }

    public void setLastLaunch(Date lastLaunch) {
        this.lastLaunch = lastLaunch;

    }
}
