package com.besttime.json;
import java.io.*;

public class Json {

    public void serialize(String name, Object object){
        try{
            FileOutputStream fs = new FileOutputStream(name+".ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(object);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object deserialize(String name){
        try {
            FileInputStream strumienPlk = new FileInputStream(name+".ser");
            ObjectInputStream os = new ObjectInputStream(strumienPlk);
            Object obj = os.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    public static void main(String args[]){

        Contact contact = new Contact(1,"Pawel", "535231060");
        ContactEntry contactEntry = new ContactEntry(contact);

        Json json = new Json();
        json.serialize("asd", contactEntry);
        ContactEntry con = (ContactEntry) json.deserialize("asd");
        System.out.println(con.getAvailability().getCurrentDay());



    }

     */

}