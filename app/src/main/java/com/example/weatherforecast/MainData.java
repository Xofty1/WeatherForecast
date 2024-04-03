package com.example.weatherforecast;

public class MainData {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private int pressure;
    private int sea_level;
    private int grnd_level;
    private int humidity;
    private double temp_kf;

    public double getTemp() {
        return temp - 273.15;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public int getPressure() {
        return pressure;
    }

    // Геттеры и сеттеры
}
