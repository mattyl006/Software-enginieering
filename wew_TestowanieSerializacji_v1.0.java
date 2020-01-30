package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.json.simple.parser.JSONParser;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Testowanie_serializacji {
    private Jc jc1, jc2, jc3, jc4, jc5;
    private Jc2 jc21;
    private ArrayList<Jc> lista_jc;
    private ArrayList<Jc2> lista_jc2;
    private Klasa_z_listami_obiektow obiekt_z_listami;
    private Json json;

    public Testowanie_serializacji() {
    }

    @Before
    public void setUp(){
        jc1 = new Jc("test1", "test1", 1);
        jc2 = new Jc("", "test2", 2);
        jc3 = new Jc("test3", "", 3);
        jc4 = new Jc("", "",4);
        jc5 = new Jc("TEST5", "TEST5", 51231232);
        jc21 = new Jc2(1,"Jan","Kowalski","123456789",25,
                new DateTime(2018, 5, 5, 10, 11, 12, 123));
        obiekt_z_listami = new Klasa_z_listami_obiektow(1,"obiekt z listami obiektow");
        lista_jc = new ArrayList<Jc>();
        lista_jc2 = new ArrayList<Jc2>();
        lista_jc.add(jc1);
        lista_jc.add(jc5);
        lista_jc2.add(jc21);
        obiekt_z_listami.setLista_jc(lista_jc);
        obiekt_z_listami.setLista_jc2(lista_jc2);

        json = new Json("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json"); //tutaj sciezka do utworzonego jsona
    }

    @Test
    public void zapis_do_json() throws IOException{
        File json_file_expected_result = new File("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json\\json_file_expected_result.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(json_file_expected_result, jc21);

        json.serialize("json",jc21);

        File json_file = new File("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json\\json.json");
        Assert.assertTrue(FileUtils.contentEquals(json_file_expected_result,json_file));
    }

    @Test
    public void odczyt_z_json() throws IOException, ParseException {

        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        assertEquals(mapper1.writeValueAsString(json.deserialize("json")), mapper2.writeValueAsString(jc1));
    }

    @Test
    public void serializuj_jeden_obiekt() throws IOException{
        json.serialize("json",obiekt_z_listami);
    }

    @Test//blad podczas konwersji typow
    public void deserializuj_jeden_obiekt() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        Json<Klasa_z_listami_obiektow> json1 = new Json<>("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json");
       Klasa_z_listami_obiektow obj = mapper2.readValue(mapper1.writeValueAsString(json1.deserialize("json")),Klasa_z_listami_obiektow.class);
       System.out.println(obj.getNazwa());
    }

    @Test
    public void zapis_do_json_dwoch_obiektow_naraz() throws IOException{

        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json\\json_file_expected_result.json",true));
        writer.write(mapper1.writeValueAsString(jc1));
        writer.write(mapper2.writeValueAsString(jc1));
        writer.close();


        json.serialize("json", Set.of(jc1,jc1));
        //json.serialize("json",jc1);
		//json.serialize("json",jc1);

        File json_file_expected_result = new File("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json\\json_file_expected_result.json");
        File json_file = new File("C:\\Users\\adaosi\\IdeaProjects\\Serializacja_Deserializacja_Json\\json.json");
        Assert.assertTrue(FileUtils.contentEquals(json_file_expected_result,json_file));
    }

    @Test
    public void test1() throws IOException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        json.serialize("json", jc1);
        assertEquals(mapper1.writeValueAsString(json.deserialize("json")), mapper2.writeValueAsString(jc1));

    }

    @Test
    public void test2() throws IOException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        json.serialize("json", jc2);
        assertEquals(mapper1.writeValueAsString(json.deserialize("json")), mapper2.writeValueAsString(jc2));

    }

    @Test
    public void test3() throws IOException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        json.serialize("json", jc3);
        assertEquals(mapper1.writeValueAsString(json.deserialize("json")), mapper2.writeValueAsString(jc3));

    }

    @Test
    public void test4() throws IOException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        json.serialize("json", jc4);
        assertEquals(mapper1.writeValueAsString(json.deserialize("json")), mapper2.writeValueAsString(jc4));

    }

    @Test
    public void test5() throws IOException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        json.serialize("json", jc5);
        assertEquals(mapper1.writeValueAsString(json.deserialize("json")), mapper2.writeValueAsString(jc5));

    }

}


