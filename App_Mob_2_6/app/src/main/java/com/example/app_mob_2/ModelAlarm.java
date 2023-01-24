package com.example.app_mob_2;

import java.util.ArrayList;

public class ModelAlarm {

    private int id;
    private int pos;

    private String name;
    private String time;
    private ArrayList<ModelDrug> drugList;

    public ModelAlarm(int id, String name, int pos, String time, ArrayList<ModelDrug> drugList){
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.time = time;
        this.drugList = drugList;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getPos(){
        return this.pos;
    }

    public String getTime(){
        return this.time;
    }

    public ArrayList<ModelDrug> getDrugList() {
        return drugList;
    }
}
