package com.example.app_mob_2;

public class ModelDrug{
    private String name;
    private int quant;

    public ModelDrug(String name, int quant){
        this.name = name;
        this.quant = quant;
    }

    public String toString(){
        return "(" + name + ", " + Integer.toString(quant) + ")";
    }

    public String getName(){
        return this.name;
    }

    public int getQuant(){
        return this.quant;
    }

}
