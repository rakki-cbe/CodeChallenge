package com.example.radhakrishnan.codechallenge;

import java.util.List;

public interface WeatherView {
    void showProgress(Boolean b);
    void updateView(List<Weather> list);
    void getDataFromDb();
    void errorInvalidCity();
    void validateCityAdd(String city);

}
