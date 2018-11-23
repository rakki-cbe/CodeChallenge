package com.example.radhakrishnan.codechallenge;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;

import com.example.radhakrishnan.codechallenge.database.DbHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTabeHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.List;

public class WeatherPresenter  implements InterFaces.weatherDataInserted {
    WeatherView v;
    InterFaces.WeatherService service;

    public WeatherPresenter(WeatherView v) {
        this.v = v;

    }

    public void setWeatherService(InterFaces.WeatherService service) {
        this.service=service;
        service.boundFromActivity();
        service.setCallBack(this);
    }




    @Override
    public void weatherDataProcessed(boolean update) {
            v.getDataFromDb();
    }

    public List<WeatherTable> getDataFromDb(SQLiteDatabase db) {
       return WeatherTabeHelper.getInstance().getWeatherDataForEachCity(db);
    }

    public void forceDataUpdate() {
        v.showProgress(true);
        service.forceDataUpdate();
    }

    public void addCity(String cityNew) {
        if(cityNew!=null && !cityNew.equals("")){
            v.validateCityAdd(cityNew);
        }
        else{
            v.errorInvalidCity();
        }
    }

    public Boolean isCityPresent(SQLiteDatabase db,String city) {
        return WeatherTabeHelper.getInstance().checkCityPresent(db,city);
    }
}
