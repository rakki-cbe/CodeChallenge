package com.example.radhakrishnan.codechallenge.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.radhakrishnan.codechallenge.Network.WeatherApiResponse;

import java.util.ArrayList;
import java.util.List;

public class WeatherTabeHelper {
    private static final WeatherTabeHelper ourInstance = new WeatherTabeHelper();
    private static final String WEATHER_TABLE = "weather";
    private static final String CITY_TABLE = "city";
    private static final String COLOMN_id = "id";
    private static final String COLOMN_REPORT = "report";
    private static final String COLOMN_REPORT_DESC = "report_desc";
    private static final String COLOMN_temp = "temp";
    private static final String COLOMN_pressure = "pressure";
    private static final String COLOMN_humidity = "humidity";
    private static final String COLOMN_tmp_min = "temp_min";
    private static final String COLOMN_tmp_max = "temp_max";
    private static final String COLOMN_time = "time";
    private static final String COLOMN_city = "city";
    private static final String COLOMN_city_id = "city_id";
    public static WeatherTabeHelper getInstance() {
        return ourInstance;
    }

    String CreateTable=" Create Table "+WEATHER_TABLE+" ("+COLOMN_id+" INTEGER PRIMARY KEY " +
            "AUTOINCREMENT,"+COLOMN_REPORT+" TEXT,"+COLOMN_REPORT_DESC+" TEXT,"+
            COLOMN_temp+" TEXT,"+COLOMN_pressure+" TEXT,"+COLOMN_humidity+" TEXT,"+
            COLOMN_tmp_min+" TEXT,"+COLOMN_tmp_max+" TEXT,"+COLOMN_time+" TEXT,"+
            COLOMN_city+" TEXT,"+COLOMN_city_id+" TEXT)";
    String DropTable=" Drop table "+WEATHER_TABLE;
    String CreateTableCity="Create table "+CITY_TABLE+"( "+COLOMN_city+" TEXT PRIMARY KEY)";
    String DropTableCity=" Drop table "+CITY_TABLE;
    private WeatherTabeHelper() {
    }

    public boolean insertToDb(SQLiteDatabase db,WeatherApiResponse response) {
        ContentValues values = new ContentValues();
        values.put(COLOMN_city_id, response.getId());
        values.put(COLOMN_city, response.getName());
        if(response.getWeather().size()>=1) {
            values.put(COLOMN_REPORT, response.getWeather().get(0).getMain());
            values.put(COLOMN_REPORT_DESC, response.getWeather().get(0).getDescription());
        }else{
            values.put(COLOMN_REPORT, "");
            values.put(COLOMN_REPORT_DESC, "");
        }
        values.put(COLOMN_temp, response.getMain().getTemp());
        values.put(COLOMN_pressure, response.getMain().getPressure());
        values.put(COLOMN_humidity, response.getMain().getHumidity());
        values.put(COLOMN_tmp_min, response.getMain().getTemp_min());
        values.put(COLOMN_tmp_max, response.getMain().getTemp_max());
        values.put(COLOMN_time, response.getDt());

        return db.insert(WEATHER_TABLE, null, values) > 0;
    }
    public boolean insertToCity(SQLiteDatabase db,String city) {
        ContentValues values = new ContentValues();
        values.put(COLOMN_city, city);
        return db.insert(CITY_TABLE, null, values) > 0;
    }

    public List<String> getCity(SQLiteDatabase db){
        List<String> city=new ArrayList<>();
        Cursor c=db.query(CITY_TABLE,new String[]{COLOMN_city},null,null,null,
                null,null);
        if(c!=null && c.getCount()!=0){
            c.moveToFirst();
            do{
                city.add(c.getString(c.getColumnIndex(COLOMN_city)));
            }
            while (c.moveToNext());
        }
        return  city;
    }
    public List<WeatherTable> getWeatherDataForEachCity(SQLiteDatabase db){
           Cursor c= db.query(WEATHER_TABLE,null,null,null,COLOMN_city,
                    null,COLOMN_time+" DESC");
           List<WeatherTable> data= new ArrayList<>();
           if(c!=null && c.getCount()>0){
               c.moveToNext();
               do{
                   data.add(new WeatherTable(c.getString(c.getColumnIndex(COLOMN_id)),
                           c.getString(c.getColumnIndex(COLOMN_REPORT)),
                           c.getString(c.getColumnIndex(COLOMN_REPORT_DESC)),
                           c.getString(c.getColumnIndex(COLOMN_temp)),
                           c.getString(c.getColumnIndex(COLOMN_pressure)),
                           c.getString(c.getColumnIndex(COLOMN_humidity)),
                           c.getString(c.getColumnIndex(COLOMN_tmp_min)),
                           c.getString(c.getColumnIndex(COLOMN_tmp_max)),
                           c.getLong(c.getColumnIndex(COLOMN_time)),
                           c.getString(c.getColumnIndex(COLOMN_city)),
                           c.getString(c.getColumnIndex(COLOMN_city_id))));

               }while (c.moveToNext());
           }
           return  data;

    }

    public Boolean checkCityPresent(SQLiteDatabase db, String city) {
        Cursor c = db.query(CITY_TABLE, new String[]{COLOMN_city}, COLOMN_city + " = ?", new String[]{city}, null,
                null, null);
        if (c != null && c.getCount() != 0) {
            return true;
        }
        return false;
    }
}
