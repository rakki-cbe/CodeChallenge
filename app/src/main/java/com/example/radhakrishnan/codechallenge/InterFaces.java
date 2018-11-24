package com.example.radhakrishnan.codechallenge;

import android.database.sqlite.SQLiteDatabase;

import com.example.radhakrishnan.codechallenge.database.WeatherTable;

public interface InterFaces {
    interface weatherDataInserted{
        void weatherDataProcessed(boolean update);

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

    interface WeatherView {
        void showProgress(Boolean b);

        void getDataFromDb();

        void errorInvalidCity();

        void validateCityAdd(String city);

        boolean isInterNetAvailable();

        void  pleaseEableInterNetToAddCity();

        void sorryWeCouldNotProcessYourRequest();
    }

    interface WeatherDetailView {
        void showProgress(Boolean b);

    }
}
