package com.example.radhakrishnan.codechallenge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "weatherDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WeatherTabeHelper.getInstance().CreateTable);
        db.execSQL(WeatherTabeHelper.getInstance().CreateTableCity);

        WeatherTabeHelper.getInstance().insertToCity(db,"Delhi");
        WeatherTabeHelper.getInstance().insertToCity(db,"London");
        WeatherTabeHelper.getInstance().insertToCity(db,"Tokyo");
        WeatherTabeHelper.getInstance().insertToCity(db,"New York");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WeatherTabeHelper.getInstance().DropTable);
        db.execSQL(WeatherTabeHelper.getInstance().DropTableCity);
        //onCreate(db);
    }
}
