package com.besttime.workhorse;

import com.besttime.app.ContactEntry;
import com.besttime.models.Contact;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class QueryTest {

    private QueriesType queriesTypeTest1;
    private QueriesType queriesTypeTest2;
    private QueriesType queriesTypeTest3;
    private QueriesType queriesTypeTest4;
    private static ContactEntry contactEntryTest;
    private static Context contextTest;
    private static Contact contactTest;
    private static int expectedId;
    private static boolean expectedAnswer;



    @BeforeClass
    public static void setup(){

        contactTest = new Contact(1,"Pawel", "123456789");
        contactEntryTest = new ContactEntry(contactTest);
        contextTest = new Context(contactEntryTest, new CurrentTime());
        expectedId = 1;
        expectedAnswer = true;

    }
    @Before
    public void setUpEnum(){

        queriesTypeTest1 = QueriesType.question1;
        queriesTypeTest2 = QueriesType.question2;
        queriesTypeTest3 = QueriesType.question3;
        queriesTypeTest4 = QueriesType.question4;
    }

    @Test
    public void getId_setIdTest(){
        Query queryTest = new Query(queriesTypeTest1,contextTest);
        queryTest.setId(1);
        Assert.assertEquals(expectedId, queryTest.getId());
    }

    @Test
    public void getAnswer_setAnswerTest(){
        Query queryTest = new Query(queriesTypeTest1,contextTest);
        queryTest.setAnswer(true);
        Assert.assertEquals(expectedAnswer, queryTest.getAnswer());
    }


}
