package com.example.radhakrishnan.codechallenge;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.radhakrishnan.codechallenge.database.WeatherTable;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.CustomHolder> {
    List<WeatherTable> data=new ArrayList<>();
    InterFaces.WeatherAdapterClick listener;

    public WeatherAdapter(InterFaces.WeatherAdapterClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_weather_city_item, viewGroup, false);
        return new CustomHolder(itemView,i);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder customHolder, int i) {
        if(i<getItemCount()-1)
            customHolder.setDate(data.get(i),i);
        else
            customHolder.setAddNewData(i);


    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    public List<WeatherTable> getData() {
        return data;
    }

    public void setData(List<WeatherTable> data) {
        this.data = data;
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        TextView city;
        TextView weather;
        OnItemClick listener;
        public CustomHolder(@NonNull View itemView,int pos) {
            super(itemView);
            city=itemView.findViewById(R.id.cityName);
            weather= itemView.findViewById(R.id.weather);
            listener = new OnItemClick(pos);
            itemView.setOnClickListener(listener);
        }

        public void setDate(WeatherTable weatherTable,int pos) {
            city.setText(weatherTable.getCityName());
            weather.setText(weatherTable.getReport_short());
            listener.setPotision(pos);
        }

        public void setAddNewData(int pos) {
            city.setText(city.getContext().getResources().getString(R.string.AddNew));
            weather.setText("");
            listener.setPotision(pos);
        }
    }
    class  OnItemClick implements View.OnClickListener{
        private int potision;

        public OnItemClick(int potision) {
            this.potision = potision;
        }

        public void setPotision(int potision) {
            this.potision = potision;
        }

        @Override
        public void onClick(View v) {
                if(potision<getItemCount()-1){
                    listener.itemClicked(data.get(potision));
                }
                else{
                    listener.addNewItemClicked();
                }
        }
    }
}
