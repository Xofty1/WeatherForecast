package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.weatherforecast.databinding.CityBinding;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {
    CityBinding cityBinding;

    LayoutInflater lInflater;
    Context ctx;
    ArrayList<WeatherItem> weatherItem = new ArrayList<WeatherItem>();
    public WeatherAdapter(ArrayList<WeatherItem> weatherItem, Context c) {
        this.ctx = c;
        this.weatherItem = weatherItem;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return weatherItem.size();
    }

    @Override
    public Object getItem(int i) {
        return weatherItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View tmp = view;
        if (tmp == null) {
            tmp = lInflater.inflate(R.layout.city, viewGroup, false);
        }
        cityBinding = CityBinding.bind(tmp);
        WeatherItem wI = getWeather(i);
        cityBinding.textCity.setText("Дата: " + wI.getDt_txt());
        cityBinding.textTemp.setText("Температура: " +  String.format("%.2f",wI.getMain().getTemp())+ "°");
//        cityBinding.textTemp.setText("Temperature: " + String.format("%.2f",wI.getMain().getTemp()) + "°");
        return tmp;
    }
    WeatherItem getWeather(int position) {
        return ((WeatherItem) getItem(position));
    }
}
