package com.example.weatherforecast;

public interface DataHandle {
    public void onDataFetched(String xmlData);

    public void onCurrentDataFetched(String xmlData);
}
