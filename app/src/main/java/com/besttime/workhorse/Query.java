package com.besttime.workhorse;
import java.util.ArrayList;

public class Query {

    private Boolean answer;
    private String question;
    private long id;

    private QueriesType queriesType;

    private Context context;

    public Query(QueriesType queriesType, Context context) {

        this.context = context;
        this.queriesType = queriesType;

        switch (queriesType) {
            case question1:
                this.question = "Is it more likely for you to speak with this person on a weekend than between Monday and Friday?";
                break;
            case question2:
                this.question = "Do you often speak with this person between Monday and Friday?";
                break;
            case question3:
                this.question = "Is it more likely for you to speak with this person between 4PM and 12AM than between 6AM and 4PM? ";
                break;
            case question4:
                this.question = "-";
                break;
            case question5:
                this.question = "Do you speak with this person at the same time and the same day of the week as today on a regular basis?";
                break;
        }
    }

    /**
     * Generates result based on answer field.
     * @return Returns list of DayOfTheWeek objects. These objects contain map of <Hours, Integer>.
     * If at Hours.h6_00 is :
     * - 1 it means that this hour is available
     * - 0 it means that this hout is not available
     */
    public ArrayList<DayOfTheWeek> generateResult() {
        ArrayList list = new ArrayList();

        switch (queriesType){
            case question1:
                if(answer){
                    System.out.println("1");
                    DayOfTheWeek sunday = new DayOfTheWeek(0);
                    DayOfTheWeek saturday = new DayOfTheWeek(6);
                    sunday.loadWholeDay();
                    saturday.loadWholeDay();
                    list.add(saturday);
                    list.add(sunday);
                }break;
            case question2:
                if(answer){
                    System.out.println("2");
                    DayOfTheWeek mon = new DayOfTheWeek(1);
                    DayOfTheWeek tue = new DayOfTheWeek(2);
                    DayOfTheWeek wed = new DayOfTheWeek(3);
                    DayOfTheWeek thu = new DayOfTheWeek(4);
                    DayOfTheWeek fri = new DayOfTheWeek(5);

                    mon.loadWholeDay();
                    thu.loadWholeDay();
                    tue.loadWholeDay();
                    wed.loadWholeDay();
                    fri.loadWholeDay();

                    list.add(mon);
                    list.add(tue);
                    list.add(thu);
                    list.add(wed);
                    list.add(fri);
                }break;
            case question3:
                DayOfTheWeek sun = new DayOfTheWeek(0);
                DayOfTheWeek mon = new DayOfTheWeek(1);
                DayOfTheWeek tue = new DayOfTheWeek(2);
                DayOfTheWeek wed = new DayOfTheWeek(3);
                DayOfTheWeek thu = new DayOfTheWeek(4);
                DayOfTheWeek fri = new DayOfTheWeek(5);
                DayOfTheWeek sat = new DayOfTheWeek(6);

                if(answer){
                    System.out.println("3.true");
                    sun.loadHalfDay(true);
                    mon.loadHalfDay(true);
                    tue.loadHalfDay(true);
                    wed.loadHalfDay(true);
                    thu.loadHalfDay(true);
                    fri.loadHalfDay(true);
                    sat.loadHalfDay(true);
                }else {
                    System.out.println("3.false");
                    sun.loadHalfDay(false);
                    mon.loadHalfDay(false);
                    tue.loadHalfDay(false);
                    wed.loadHalfDay(false);
                    thu.loadHalfDay(false);
                    fri.loadHalfDay(false);
                    sat.loadHalfDay(false);
                }

                list.add(sun);
                list.add(mon);
                list.add(tue);
                list.add(thu);
                list.add(fri);
                list.add(sat);
                list.add(wed);
                break;

            case question5:
                if(answer) {
                    System.out.println("5");
                    int hour = context.getTime().getHours();
                    int min = context.getTime().getMinutes();
                    int dayId = context.getTime().getDay();

                    DayOfTheWeek day = new DayOfTheWeek(dayId);
                    day.loadOneTime(hour, min);
                    list.add(day);
                }break;

        }
        return list;
    }


    public Boolean getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public long getId() {
        return id;
    }

    public QueriesType getQueriesType() {
        return queriesType;
    }

    public Context getContext() {
        return context;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public void setId(long id) {
        this.id = id;
    }
/*
    public static void main(String[] args) {
        ContactEntry contactEntry = new ContactEntry();
        Context context = new Context(contactEntry);
        QueriesType queriesType = QueriesType.question1;

        Query query = new Query(queriesType, context);

        System.out.println(query.getQuestion());    //pokazuje pytanie
        System.out.println(query.getAnswer());      //prawda / falsz pytania / brak danych jako null
        query.setAnswer(true);                      //ustawia prawda / falsz
        System.out.println(query.getAnswer());      //wyswietla odpowiedz
        DayOfTheWeek a = (DayOfTheWeek) query.generateResult().get(0);
        System.out.println(a.getMap());
    }
*/
}
