package com.besttime.json;
import android.content.Context;

import java.io.*;

public class Json {


    Context androidContext;

    public Json(Context androidContext) {
        this.androidContext = androidContext;
    }

    public void serialize(String name, Object object){
        try{


            FileOutputStream fs = androidContext.openFileOutput(name+".ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(object);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object deserialize(String name) throws IOException, ClassNotFoundException {
        try {

            FileInputStream strumienPlk = androidContext.openFileInput(name+".ser");
            ObjectInputStream os = new ObjectInputStream(strumienPlk);
            Object obj = os.readObject();
            return obj;
        } catch (Exception e) {
            throw e;
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