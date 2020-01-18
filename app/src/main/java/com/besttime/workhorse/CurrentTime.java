package com.besttime.workhorse;

import java.util.Calendar;
import java.util.Date;

public class CurrentTime {
    private Date time;

    public CurrentTime(){

    }

    public Date getTime() {
        this.time = Calendar.getInstance().getTime();
        return this.time;
    }

    public Integer getDayOfWeekAsDecimal(){
        return getTime().getDay();                      // 0 to niedziela i tak dalej 6 sobota
    }
}
