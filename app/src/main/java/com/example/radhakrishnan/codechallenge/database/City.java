package com.example.radhakrishnan.codechallenge.database;

public class City {
    private String name="";
    private  String id="";
    public City(String name,String id){
        this.name=name;
        this.id=id;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
