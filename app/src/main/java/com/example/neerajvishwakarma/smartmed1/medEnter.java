package com.example.neerajvishwakarma.smartmed1;

/**
 * Created by neerajvishwakarma on 02/12/17.
 */

public class medEnter {
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

    public medEnter(){

    }

    public medEnter(int id,String name){
        this.id = id;
        this.name = name;
    }


}
