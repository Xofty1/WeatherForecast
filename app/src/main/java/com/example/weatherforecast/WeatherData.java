package com.example.weatherforecast;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherData {
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;

    public String getName() {
        return name;
    }

    public Main getMain() {
        return main;
    }

    public static class Coord {
        private double lon;
        private double lat;

        // Getters and setters
        // (You can generate them automatically in your IDE or write them manually)
    }

    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        // Getters and setters
    }

    public static class Main {
        private double temp;
        @SerializedName("feels_like")
        private double feelsLike;
        @SerializedName("temp_min")
        private double tempMin;
        @SerializedName("temp_max")
        private double tempMax;
        private int pressure;
        private int humidity;
        @SerializedName("sea_level")
        private int seaLevel;
        @SerializedName("grnd_level")
        private int groundLevel;

        public double getTemp() {
            return temp-273.15;
        }

        public int getPressure() {
            return pressure;
        }
// Getters and setters
    }

    public static class Wind {
        private double speed;
        private int deg;
        private double gust;

        // Getters and setters
    }

    public static class Rain {
        @SerializedName("1h")
        private double oneHour;

        // Getters and setters
    }

    public static class Clouds {
        private int all;

        // Getters and setters
    }

    public static class Sys {
        private int type;
        private int id;
        private String country;
        private long sunrise;
        private long sunset;

        // Getters and setters
    }

    // Getters and setters for all fields
}
