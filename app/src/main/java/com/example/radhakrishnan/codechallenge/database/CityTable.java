package com.example.radhakrishnan.codechallenge.database;

public class CityTable {
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public CityTable(String cityName) {

        this.cityName = cityName;
    }
}