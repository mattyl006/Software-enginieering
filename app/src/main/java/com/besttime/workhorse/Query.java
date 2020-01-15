package com.besttime.workhorse;

import java.util.ArrayList;


public class Query {
    private ArrayList <Boolean> answer;
    private ArrayList <String> questions;

    private String firstQuestion;
    private boolean firstAnswer;

    public Query(Context context){
        answer = new ArrayList<Boolean>();
        questions = new ArrayList<String>();

        questions.add("Czy regularnie rozmawia Pan z ta osoba o tej samej godzinie i dniu tygodnia co dzisiaj?");
        questions.add("Czy rozmawia Pan z ta osoba czesciej w weekendy niż od poniedzialku do piatku? ");
        questions.add("Czy czesto rozmawia Pan z ta osoba od poniedzialku do piatku?");
        questions.add("Czy rozmawia Pan z ta osoba czesciej w godzinach 16:00-24:00 niz w godzinach 6:00-16:00? ");
        questions.add("Czy ta osoba zazwyczaj jest dostepna i moze rozmawiac, gdy Pan do niej dzwoni?");

        firstQuestion = "Czy regularnie rozmawia Pan z tą osoba o tej samej godzinie i dniu tygodnia co dzisiaj? ";
    }

    public String getFristQuestion(){
        return firstQuestion;
    }

    public void setFirstAnswer(boolean answer){
        firstAnswer = answer;
    }

    public boolean getFirstAnswer(){
        return firstAnswer;
    }


    public String getQuestion(int numOfQuestion){
        return questions.get(numOfQuestion);
    }

    public void setAnswer(boolean answerToQuestion) {
        answer.add(answerToQuestion);
    }

    public boolean getAnswer(int answerToQuestion){
        return answer.get(answerToQuestion);
    }

    public static void main(String[] args) {

        Context context = new Context();

        Query query = new Query(context);

        System.out.println(query.getQuestion(1));

        query.setAnswer(true);
        query.setAnswer(false);
        query.setAnswer(true);

        System.out.println(query.getAnswer(0));
        System.out.println(query.getAnswer(1));
        System.out.println(query.getAnswer(2));

    }



}
