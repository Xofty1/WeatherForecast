package com.example.weatherforecast;

public class Main {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private int pressure;
    private int humidity;
    private int sea_level;
    private int grnd_level;

    public double getTemp() {
        return temp - 273.15;
    }

    public double getFeels_like() {
        return feels_like - 273.15;
    }

    public double getPressure() {
        return pressure *  0.75006375541921;
    }

    public int getHumidity() {
        return humidity;
    }
// Геттеры и сеттеры
}
