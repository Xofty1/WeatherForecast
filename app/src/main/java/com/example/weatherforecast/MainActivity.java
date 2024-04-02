package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.weatherforecast.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    private static final String URL = "http://worldweather.wmo.int/ru/json/206_ru.xml";
    ActivityMainBinding binding;
    private final static String FILE_NAME = "weather.json";
    WeatherData weatherData;
    WeatherLongData weatherLongData;
    static String info = "{\"coord\":{\"lon\":37.6177,\"lat\":55.752},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":290.51,\"feels_like\":290,\"temp_min\":289.31,\"temp_max\":291.73,\"pressure\":1004,\"humidity\":65,\"sea_level\":1004,\"grnd_level\":986},\"visibility\":10000,\"wind\":{\"speed\":2.82,\"deg\":182,\"gust\":5.78},\"clouds\":{\"all\":68},\"dt\":1712045001,\"sys\":{\"type\":2,\"id\":2000314,\"country\":\"RU\",\"sunrise\":1712026621,\"sunset\":1712074122},\"timezone\":10800,\"id\":524901,\"name\":\"Moscow\",\"cod\":200}";
    static String weather;
    ArrayList<WeatherData> weatherDataArrayList = new ArrayList<>();
    String city = "Moscow";
    private View headerView = null;
    View.OnClickListener fetch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new FetchDataTask(MainActivity.this, city).execute();
        }
    };

    TextWatcher cityChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Вызывается перед тем, как текст изменится
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Вызывается во время изменения текста
            city = s.toString();
            // Здесь можно выполнить какие-либо действия при изменении текста
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Вызывается после того, как текст изменился
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        new FetchDataTask(this, city).execute();// для динамеческой работы
        binding.buttonFetch.setOnClickListener(fetch);
        binding.editTextCityName.addTextChangedListener(cityChanged);
    }


    public void onDataFetched(String xmlData) {
        if (xmlData != null) {
            weather = xmlData;
            Gson gson = new Gson();
            weatherLongData = gson.fromJson(weather, WeatherLongData.class);
//            weatherDataArrayList.add(weatherLongData);
            WeatherAdapter wA = new WeatherAdapter(weatherLongData.getList(), this);
//            WeatherAdapter wA = new WeatherAdapter(weatherDataArrayList, this);
            binding.lvMain.setAdapter(wA);
            if (headerView == null) {
                headerView = createHeader(weatherLongData.getCity().getName());
                binding.lvMain.addHeaderView(headerView);
            } else {
                updateHeader(headerView, weatherLongData.getCity().getName());
            }

//            binding.text.setText(weatherData.getName());
            Log.d("XML Data", xmlData);
        } else {
            Log.e("Error", "Failed 23 to fetch data from the server");
        }
    }
    private void updateHeader(View header, String cityName) {
        ((TextView) header.findViewById(R.id.textViewCity)).setText(cityName);
    }
    View createHeader(String text) {
        View v = getLayoutInflater().inflate(R.layout.header, null);
        ((TextView) v.findViewById(R.id.textViewCity)).setText(text);
        return v;
    }

}
