package com.besttime.workhorse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FormManager {

    private List<Form> sentForms;

    public List<Form> getSentForms() {
        return sentForms;
    }

    public FormManager(){
        this.sentForms = new ArrayList<>();
    }


    public void addNewFormToSentForms(Form newForm){
        sentForms.add(newForm);
    }


    /**
     *
     * @param formId
     * @return null if form hasn't been filled yet.
     */
    public Form getSentFormWithId(Long formId){
        for (Form form :
                sentForms) {
            if(formId == form.getId()){
                return form;
            }
        }

        return null;
    }

    private List<DayOfTheWeek> parseFetchedRowFromSheet(List<String> row){


        return null;
    }

}