package com.example.radhakrishnan.codechallenge.Network;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.radhakrishnan.codechallenge.database.DbHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTabeHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WeatherServiceHelper {
    NetworkHelper networkHelper;
    SQLiteOpenHelper db;
    Gson gson;

    public WeatherServiceHelper(DbHelper db) {
        networkHelper=new NetworkHelper();
        this.db = db;
        this.gson = new Gson();
    }

    public boolean fetchData() throws IOException {
        boolean isSucess =true;
        List<String> city = WeatherTabeHelper.getInstance().getCity(db.getReadableDatabase());
        for (String item:city
             ) {


            String result = networkHelper.downloadUrl(
                    new URL(
                            "http://api.openweathermap.org/data/2.5/weather?" +
                                    "q="+item+"&APPID=cb26d2d738eff8b012070fa6be70d17a"));
            if (result != null && !result.equals("")) {
                WeatherApiResponse response = gson.fromJson(result, WeatherApiResponse.class);
                boolean b = WeatherTabeHelper.getInstance().insertToDb(db.getWritableDatabase(), response);
                if(b==false)
                 isSucess=false;
            }
        }
        return  isSucess;
    }
    public boolean fetchData(String city) throws IOException {
        boolean isSucess =true;
        SQLiteDatabase writDb =db.getWritableDatabase();
          String result = networkHelper.downloadUrl(
                    new URL(
                            "http://api.openweathermap.org/data/2.5/weather?" +
                                    "q="+city+"&APPID=cb26d2d738eff8b012070fa6be70d17a"));
            if (result != null && !result.equals("")) {
                WeatherApiResponse response = gson.fromJson(result, WeatherApiResponse.class);
                boolean b = WeatherTabeHelper.getInstance().insertToDb(writDb, response);
                if(b==false) {
                    isSucess = false;
                    WeatherTabeHelper.getInstance().insertToCity(writDb,city);
                }

            }

        return  isSucess;
    }
}
