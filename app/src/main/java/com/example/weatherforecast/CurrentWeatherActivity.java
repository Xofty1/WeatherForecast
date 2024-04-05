package com.example.weatherforecast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.example.weatherforecast.databinding.ActivityCurrentWeatherBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class CurrentWeatherActivity extends AppCompatActivity implements DataHandle {
    ActivityCurrentWeatherBinding binding;
    String currentWeather;
    WeatherData weatherData;
    String city;
    View.OnClickListener fetch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(binding.editTextCity.getText())) {
                binding.editTextCity.setText("Moscow");
                city = "Moscow";
            }
            new FetchCurrentData(CurrentWeatherActivity.this, CurrentWeatherActivity.this, city, 0, 0, RequestType.CITY).execute();
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
        binding = ActivityCurrentWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonCurrentWeather.setOnClickListener(fetch);
        binding.editTextCity.addTextChangedListener(cityChanged);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        if (city == null)
            city = "Moscow";
        new FetchCurrentData(this, this, city, 0, 0, RequestType.CITY).execute();

        Intent intentToWeather = new Intent(this, MainActivity.class);
        binding.buttonToWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentToWeather);
            }
        });
    }

    @Override
    public void onDataFetched(String xmlData) {

    }

    @Override
    public void onCurrentDataFetched(String xmlData) {
        if (xmlData != null) {
            currentWeather = xmlData;
            Gson gsonCurrent = new Gson();
            weatherData = gsonCurrent.fromJson(currentWeather, WeatherData.class);
            binding.textCurrent.setText(weatherData.getName());
            binding.textTemp.setText("Температура: " + String.format("%.2f", weatherData.getMain().getTemp()) + "°");
            binding.textFeelLike.setText("Чувствуется как: " + String.format("%.2f", weatherData.getMain().getFeels_like()) + "°");
            binding.textHumidity.setText("Влажность: " + weatherData.getMain().getHumidity() + "%");
            binding.textPressure.setText("Давление: " + String.format("%.2f", weatherData.getMain().getPressure()) + " мм рт.ст.");
            Picasso.get().load("https://openweathermap.org/img/wn/" + weatherData.getWeather().get(0).getIcon() + "@2x.png").into(binding.imageWeather);
        }
    }
}