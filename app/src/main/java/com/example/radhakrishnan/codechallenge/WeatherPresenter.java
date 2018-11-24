package com.example.radhakrishnan.codechallenge;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;

import com.example.radhakrishnan.codechallenge.database.DbHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTabeHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.List;

public class WeatherPresenter  implements InterFaces.weatherDataInserted {
    InterFaces.WeatherView v;
    InterFaces.WeatherService service;

     WeatherPresenter(InterFaces.WeatherView v) {
        this.v = v;

    }

     void setWeatherService(InterFaces.WeatherService service) {
        this.service=service;
        service.boundFromActivity();
        service.setCallBack(this);
    }




    @Override
    public void weatherDataProcessed(boolean update) {
         if(update)
            v.getDataFromDb();
         else {
             v.showProgress(false);
             v.sorryWeCouldNotProcessYourRequest();
         }
    }

     List<WeatherTable> getDataFromDb(SQLiteDatabase db) {
       return WeatherTabeHelper.getInstance().getWeatherDataForEachCity(db);
    }

     void forceDataUpdate() {
         if(v.isInterNetAvailable()) {
             v.showProgress(true);
             service.forceDataUpdate();
         }else{
             v.getDataFromDb();
             v.pleaseEableInterNetToAddCity();
         }
    }

     void addCity(String cityNew) {
        if(cityNew!=null && !cityNew.equals("")){
            v.validateCityAdd(cityNew);
        }
        else{
            v.errorInvalidCity();
        }
    }

     Boolean isCityPresent(SQLiteDatabase db,String city) {
        return WeatherTabeHelper.getInstance().checkCityPresent(db,city);
    }

     void addNewCity(String city) {
        if(v.isInterNetAvailable()) {
            service.getDataForOnceCity(city);
        }
        else{
            v.pleaseEableInterNetToAddCity();
            v.showProgress(true);
        }
    }
}
