
package com.example.weatherforecast;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchCurrentData extends AsyncTask<Void, Void, String> {
    private String Url = "https://api.openweathermap.org/data/2.5/weather?q=Moscow&lang=ru&appid=a1c34ffcfd0d837993551913ad415dc0";
    //    private static final String Url = "https://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=a1c34ffcfd0d837993551913ad415dc0";
//    private static final String Url = "https://api.openweathermap.org/data/2.5/weather?lat=55.7522200&lon=37.6155600&appid=a1c34ffcfd0d837993551913ad415dc0";
//    private static final String URL = "https://api.vk.com/method/users.get";
    double latitude;
    double longitude;
    private DataHandle activity;

    private Context context;

    public FetchCurrentData(Context context, DataHandle activity, String city, double latitude, double longitude, RequestType requestType) {
        this.activity = activity;
        this.context = context;
        if (requestType == RequestType.CITY) {
            this.Url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=ru&appid=a1c34ffcfd0d837993551913ad415dc0";
        } else if (requestType == RequestType.COORD) {
            this.Url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&lang=ru&appid=a1c34ffcfd0d837993551913ad415dc0";
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;
        String info = null;
        try {
            URL url = new URL(Url);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                inputStream = httpsURLConnection.getInputStream();
                info = convertInputStreamToString(inputStream);
            }
        } catch (IOException e) {
//            Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
            } catch (IOException e) {
//                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return info;
    }

    @Override
    protected void onPostExecute(String xmlData) {
        if (xmlData != null) {
            activity.onCurrentDataFetched(xmlData);
            Log.d("XMLData", xmlData);
        } else {
            Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_LONG).show();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}
