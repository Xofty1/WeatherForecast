package com.example.weatherforecast;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class WeatherApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("a075c213-cf50-41fb-994d-60f735f08f1b");
    }
}
