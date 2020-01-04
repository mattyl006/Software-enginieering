package com.besttime.models;

public class Contact {

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
}
