package com.besttime.workhorse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FormManager {

    private Map<Long,Date> sentForms;

    public Map<Long, Date> getSentForms() {
        return sentForms;
    }

    public FormManager(){
        this.sentForms = new HashMap<Long,Date>();
    }


    public void addToMap(Long formId, Date date){
        try {

            this.sentForms.put(formId,date);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }


    /**
     *
     * @param formId
     * @return null if form hasn't been filled yet.
     */
    public Date findDateWhenFormWasFilled(Long formId){
        try {
            return this.sentForms.get(formId);
        }
        catch (Exception e )
        {
            System.out.println(e);
        }
        return null;
    }

}