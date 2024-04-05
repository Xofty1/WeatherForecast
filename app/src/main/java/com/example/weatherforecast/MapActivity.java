package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.databinding.ActivityMainBinding;
import com.example.weatherforecast.databinding.ActivityMapBinding;
import com.google.gson.Gson;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MapActivity extends AppCompatActivity implements InputListener, DataHandle {
    ActivityMapBinding mapBinding;
    static String weather;
    private MapView mapView;
    private View headerView = null;
    WeatherLongData weatherLongData;
    private double latitude = 55.75222;
    private double longitude = 37.61556;
    private MapObjectTapListener placemarkTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            Toast.makeText(
                    MapActivity.this,
                    "Tapped the point (" + point.getLongitude() + ", " + point.getLatitude() + ")",
                    Toast.LENGTH_SHORT
            ).show();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);
        mapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(mapBinding.getRoot());
        mapView = mapBinding.mapView;
        mapView.getMap().move(new CameraPosition(new Point(latitude, longitude), 5.0F, 0.0F, 0.0F));
        mapView.getMap().addInputListener(this);
        Intent intent = getIntent();
        weather = intent.getStringExtra("data");
        onDataFetched(weather);
        if (weather != null)
            mapView.getMap().move(new CameraPosition(new Point(weatherLongData.getCity().getCoord().getLat(), weatherLongData.getCity().getCoord().getLon()), 5.0F, 0.0F, 0.0F));
        Intent intentToSearch = new Intent(this, MainActivity.class);
        mapBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToSearch.putExtra("data", weather);
                startActivity(intentToSearch);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();

    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        mapView.getMap().getMapObjects().clear();
        Resources resources = getResources();
        Bitmap originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.tag);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 50, 50, false);
        mapView.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromBitmap(resizedBitmap));
        new FetchDataTask(this, this, "", latitude, longitude, RequestType.COORD).execute();
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }
    @Override
    public void onDataFetched(String xmlData) {
        if (xmlData != null) {
            weather = xmlData;
            Gson gson = new Gson();
            weatherLongData = gson.fromJson(weather, WeatherLongData.class);
            WeatherAdapter wA = new WeatherAdapter(weatherLongData.getList(), this);
            mapBinding.lvMain.setAdapter(wA);
            if (headerView == null) {
                headerView = createHeader(weatherLongData.getCity().getName());
                mapBinding.lvMain.addHeaderView(headerView);
            } else {
                updateHeader(headerView, weatherLongData.getCity().getName());
            }
        }
    }

    @Override
    public void onCurrentDataFetched(String xmlData) {

    }

    View createHeader(String text) {
        View v = getLayoutInflater().inflate(R.layout.header, null);
        ((TextView) v.findViewById(R.id.textViewCity)).setText(text);
        return v;
    }

    private void updateHeader(View header, String cityName) {
        ((TextView) header.findViewById(R.id.textViewCity)).setText(cityName);
    }
}