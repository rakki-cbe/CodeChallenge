package com.example.radhakrishnan.codechallenge;

import android.database.sqlite.SQLiteDatabase;

import com.example.radhakrishnan.codechallenge.database.WeatherTabeHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.List;

public class WeatherDetailPresenter implements InterFaces.weatherDataInserted {
    InterFaces.WeatherService service;
    private SQLiteDatabase db;
    private InterFaces.WeatherDetailView v;
    private String city;

    WeatherDetailPresenter(InterFaces.WeatherDetailView v) {
        this.v = v;

    }

    void setWeatherService(InterFaces.WeatherService service) {
        this.service = service;
        service.boundFromActivity();
        service.setCallBack(this);
        db = service.getReadDataBase();

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void weatherDataProcessed(boolean update) {

    }

    List<WeatherTable> getDataFromDb() {
        return WeatherTabeHelper.getInstance().getWeatherHistoryDataForCity(db, city);
    }


}
