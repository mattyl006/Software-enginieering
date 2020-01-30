package com.company;

import java.util.ArrayList;

public class Klasa_z_listami_obiektow {
    private int id;
    private String nazwa;
    private ArrayList<Jc> lista_jc;
    private ArrayList<Jc2> lista_jc2;

    Klasa_z_listami_obiektow(int id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public ArrayList<Jc> getLista_jc() {
        return lista_jc;
    }

    void setLista_jc(ArrayList<Jc> lista_jc) {
        this.lista_jc = lista_jc;
    }

    public ArrayList<Jc2> getLista_jc2() {
        return lista_jc2;
    }

    void setLista_jc2(ArrayList<Jc2> lista_jc2) {
        this.lista_jc2 = lista_jc2;
    }
}
