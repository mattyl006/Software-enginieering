package com.besttime.models;

import java.io.Serializable;

public class Contact implements Serializable {

    private int mId;
    private String mName;
    private String mPhoneNumber;


    public Contact(int id, String name, String phoneNumber) {
        this.mId = id;
        this.mName = name;
        this.mPhoneNumber = phoneNumber;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void changeName(String newName){
        mName = newName;
    }
}
