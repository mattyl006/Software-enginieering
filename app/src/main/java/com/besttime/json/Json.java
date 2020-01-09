package com.besttime.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class Json<T> {

    private String path; //path nie konczy sie "/"

    public Json(String folderPath) {
        this.path = folderPath;

    }

    public String getPath() {
        return path;
    }

    public void serialize(String filename, T object) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.writeValue(new File(this.getPath() + "/" + filename + ".json"), object);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public T deserialize(String jsonPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        T object = null;
        try {
            object = objectMapper.reader().forType(new TypeReference<T>() {
            }).readValue(new File(this.getPath() + "/" + jsonPath + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

}
//TODO  to jest main
/*
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Jc jc = new Jc("test1", "test2", 123);
        Json json = new Json("..."); //tutaj sciezka do utworzonego jsona

        json.serialize("jc2", jc);
        System.out.println( json.deserialize("jc2")); // nazwa pliku json

        System.out.println(jc.getX());


    }
}


TODO to jest przykladowa klasa, ktora bedziemy serializowac i deserializowac

public class Jc {

    String x;
    String y;
    int z;

    public Jc(String x1, String y1, int z1){
        this.x = x1;
        this.y = y1;
        this.z = z1;
    }

        public String getX() {
        return x;
    }
}

 */