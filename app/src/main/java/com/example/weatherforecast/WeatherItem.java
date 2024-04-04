package com.example.weatherforecast;

import java.util.ArrayList;

public class WeatherItem {
    private long dt;
    private MainData main;
    private ArrayList<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private int visibility;
    private double pop;
    private Sys sys;
    private String dt_txt;

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public MainData getMain() {
        return main;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    // Геттеры и сеттеры
}
