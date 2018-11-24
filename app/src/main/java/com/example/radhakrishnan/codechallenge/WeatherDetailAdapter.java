package com.example.radhakrishnan.codechallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeatherDetailAdapter extends RecyclerView.Adapter<WeatherDetailAdapter.CustomHolder> {
    List<WeatherTable> data = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    private Calendar calendar = Calendar.getInstance();


    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_weather_detail_item, viewGroup, false);
        return new CustomHolder(itemView, i);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder customHolder, int i) {
        customHolder.setDate(data.get(i), i);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<WeatherTable> getData() {
        return data;
    }

    public void setData(List<WeatherTable> data) {
        this.data = data;
    }

    private String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    class CustomHolder extends RecyclerView.ViewHolder {
        TextView temp, weather, humidity, tmp_min, tmp_max, time;

        CustomHolder(@NonNull View itemView, int pos) {
            super(itemView);
            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            tmp_min = itemView.findViewById(R.id.temp_min);
            tmp_max = itemView.findViewById(R.id.temp_max);
            time = itemView.findViewById(R.id.time);
            weather = itemView.findViewById(R.id.weather);

        }

        void setDate(WeatherTable weatherTable, int pos) {
            Context context = temp.getContext();
            temp.setText(String.format(context.getResources().getString(R.string.Temp),
                    weatherTable.getTemp()));
            humidity.setText(String.format(context.getResources().getString(R.string.humidity),
                    weatherTable.getHumidity()));
            tmp_max.setText(String.format(context.getResources().getString(R.string.temp_max),
                    weatherTable.getTemp_max()));
            tmp_min.setText(String.format(context.getResources().getString(R.string.temp_min),
                    weatherTable.getTemp_min()));
            time.setText(getDate(weatherTable.getTime() * 1000));
            weather.setText(weatherTable.getReport_short());

        }


    }


}
