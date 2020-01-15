package com.besttime.workhorse;

import java.util.Calendar;
import java.util.Date;


public class Context {

    private Date time;

    public Context(){
        this.time = Calendar.getInstance().getTime();
    }

    public Date getTime() {
        return time;
    }

}