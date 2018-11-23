package com.example.radhakrishnan.codechallenge.database;

public class WeatherTable {
    private  String id;
    private  String report_short;
    private  String report_description;
    private  String temp;
    private  String pressure;
    private  String humidity;
    private  String temp_min;
    private  String temp_max;
    private  long time;
    private  String cityName;
    private  String cityId;

    public WeatherTable(String id, String report_short, String report_description, String temp,
                        String pressure, String humidity, String temp_min, String temp_max,
                        long time,String cityName,String cityId) {
        this.id = id;
        this.report_short = report_short;
        this.report_description = report_description;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.time = time;
        this.cityName =cityName;
        this.cityId =cityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReport_short() {
        return report_short;
    }

    public void setReport_short(String report_short) {
        this.report_short = report_short;
    }

    public String getReport_description() {
        return report_description;
    }

    public void setReport_description(String report_description) {
        this.report_description = report_description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
