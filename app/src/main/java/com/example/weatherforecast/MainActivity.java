package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements DataHandle {
    ActivityMainBinding binding;
    WeatherLongData weatherLongData;
    WeatherData weatherData;
    String currentWeather;
    String weather;
    String city = "Moscow";
    private View headerView = null;
    View.OnClickListener fetch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(binding.editTextCityName.getText())) {
                binding.editTextCityName.setText("Moscow");
                city = "Moscow";
            }
            new FetchCurrentData(MainActivity.this, MainActivity.this, city, 0, 0, RequestType.CITY).execute();
            new FetchDataTask(MainActivity.this, MainActivity.this, city, 0, 0, RequestType.CITY).execute();
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

        binding.buttonFetch.setOnClickListener(fetch);
        binding.editTextCityName.addTextChangedListener(cityChanged);
        Intent intent = getIntent();
        weather = intent.getStringExtra("data");
        if (weather != null) {
            onDataFetched(weather);
            new FetchCurrentData(MainActivity.this, MainActivity.this, city, 0, 0, RequestType.CITY).execute();

        } else {
            new FetchCurrentData(MainActivity.this, MainActivity.this, city, 0, 0, RequestType.CITY).execute();
            new FetchDataTask(this, this, city, 0, 0, RequestType.CITY).execute();// для динамеческой работы
        }
        Intent intentToMap = new Intent(this, MapActivity.class);
        binding.buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToMap.putExtra("data", weather);
                startActivity(intentToMap);
            }
        });
        Intent intentToCurrentWeather = new Intent(this, CurrentWeatherActivity.class);
        binding.buttonToCurrentWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToCurrentWeather.putExtra("city", weatherLongData.getCity().getName());
                startActivity(intentToCurrentWeather);
            }
        });
    }


    public void onDataFetched(String xmlData) {
        if (xmlData != null) {
            weather = xmlData;
            Gson gson = new Gson();
            weatherLongData = gson.fromJson(weather, WeatherLongData.class);
            //                  weatherDataArrayList.add(weatherLongData);
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

    @Override
    public void onCurrentDataFetched(String xmlData) {

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