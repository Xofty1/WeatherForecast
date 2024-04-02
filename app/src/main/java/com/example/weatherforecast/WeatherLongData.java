package com.example.weatherforecast;

import java.util.ArrayList;

public class WeatherLongData {
    private String cod;
    private int message;
    private int cnt;
    private ArrayList<WeatherItem> list;
    private City city;

    public City getCity() {
        return city;
    }

    public ArrayList<WeatherItem> getList() {
        return list;
    }
// Геттеры и сеттеры
}

