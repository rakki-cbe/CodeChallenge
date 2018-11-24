package com.example.radhakrishnan.codechallenge;

import android.database.sqlite.SQLiteDatabase;

import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.List;

public interface InterFaces {
    interface weatherDataInserted{
        public  void weatherDataProcessed(boolean update);

    }
    interface  WeatherService{
        void stopThread();
        void boundFromActivity();
        void setCallBack(weatherDataInserted callBack);
        void forceDataUpdate();
        SQLiteDatabase getReadDataBase();
        SQLiteDatabase getWriteDataBase();
        void getDataForOnceCity(String city);
    }
    interface WeatherAdapterClick{
        void itemClicked(WeatherTable item);
        void addNewItemClicked();
    }
    public interface WeatherView {
        void showProgress(Boolean b);
        void updateView(List<Weather> list);
        void getDataFromDb();
        void errorInvalidCity();
        void validateCityAdd(String city);
        boolean isInterNetAvailable();
        void  pleaseEableInterNetToAddCity();

        void sorryWeCouldNotProcessYourRequest();
    }
}
