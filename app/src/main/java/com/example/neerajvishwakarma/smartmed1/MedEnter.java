package com.example.neerajvishwakarma.smartmed1;

/**
 * Created by neerajvishwakarma on 02/12/17.
 */

public class MedEnter {
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MedEnter(){

    }

    public MedEnter(int id, String name){
        this.id = id;
        this.name = name;
    }


}
