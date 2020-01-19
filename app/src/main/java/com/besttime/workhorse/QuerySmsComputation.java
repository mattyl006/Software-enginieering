package com.besttime.workhorse;

import java.util.*;

public class QuerySmsComputation {
    private Week smsWeek;
    private Week queryWeek;
    private List<Hours> hoursList;

    public QuerySmsComputation(Week smsWeek, Week queryWeek){
        this.smsWeek = smsWeek;
        this.queryWeek = queryWeek;
        this.hoursList = Arrays.asList(Hours.values());
    }

    public void getAvailability(){

    }

    public Enum compareDays(int sms,int query){
        if(sms > 0){
            return AvailType.available;
        }else {
            if (query == 0){
                return AvailType.unavailable;
            }
            else return AvailType.perhaps;
        }
    }

    public HashMap<Hours, AvailType> getMonday(){
        Map sM = smsWeek.getMonday();
        Map qM = queryWeek.getMonday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }

    public HashMap<Hours, AvailType> getTuesday(){
        Map sM = smsWeek.getTuesday();
        Map qM = queryWeek.getTuesday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }

    public HashMap<Hours, AvailType> getWednesday(){
        Map sM = smsWeek.getWednesday();
        Map qM = queryWeek.getWednesday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }
    public HashMap<Hours, AvailType> getThursday(){
        Map sM = smsWeek.getThursday();
        Map qM = queryWeek.getThursday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }

    public HashMap<Hours, AvailType> getFriday(){
        Map sM = smsWeek.getFriday();
        Map qM = queryWeek.getFriday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }

    public HashMap<Hours, AvailType> getSaturday(){
        Map sM = smsWeek.getSaturday();
        Map qM = queryWeek.getSaturday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }

    public HashMap<Hours, AvailType> getSunday(){
        Map sM = smsWeek.getSunday();
        Map qM = queryWeek.getSunday();
        HashMap<Hours, AvailType> mMap = new HashMap<>();

        for(int i = 0; i < hoursList.size(); i++){
            int s = (int) sM.get(hoursList.get(i));
            int q = (int) qM.get(hoursList.get(i));
            Enum availability = (Enum) compareDays(s,q);
            mMap.put(hoursList.get(i), (AvailType) availability);
        }
        return mMap;
    }

    public List<Map> getWeek(){
        List<Map> week = new ArrayList();
        week.add(getSunday());
        week.add(getMonday());
        week.add(getTuesday());
        week.add(getWednesday());
        week.add(getThursday());
        week.add(getFriday());
        week.add(getSaturday());

        return week;
    }
    /*
    public static void main(String[] args) {
        DayOfTheWeek sMon = new DayOfTheWeek(1);
        DayOfTheWeek qMon = new DayOfTheWeek(1);

        sMon.loadOneTime(16,30);
        sMon.loadOneTime(17,30);
        qMon.loadOneTime(11,30);
        qMon.loadOneTime(12,20);

        Week sweek = new Week();
        Week qweek = new Week();

        sweek.updateDay(sMon);
        qweek.updateDay(qMon);

        QuerySmsComputation querySmsComputation = new QuerySmsComputation(sweek, qweek);
        System.out.println(querySmsComputation.getWeek().get(0));
        System.out.println(querySmsComputation.getWeek().get(1));
        System.out.println(querySmsComputation.getWeek().get(2));

    }
    */
}
