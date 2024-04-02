package com.example.weatherforecast;


import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchDataTask extends AsyncTask<Void, Void, String> {
    private String Url = "https://api.openweathermap.org/data/2.5/forecast?q=Moscow&appid=a1c34ffcfd0d837993551913ad415dc0";
//    private static final String Url = "https://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=a1c34ffcfd0d837993551913ad415dc0";
//    private static final String Url = "https://api.openweathermap.org/data/2.5/weather?lat=55.7522200&lon=37.6155600&appid=a1c34ffcfd0d837993551913ad415dc0";
//    private static final String URL = "https://api.vk.com/method/users.get";

    private MainActivity mainActivity;

    public FetchDataTask(MainActivity mainActivity, String city) {
        this.mainActivity = mainActivity;
        this.Url = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=a1c34ffcfd0d837993551913ad415dc0";
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
                e.printStackTrace();
            }
        }
        return info;
    }

    @Override
    protected void onPostExecute(String xmlData) {
        if (xmlData != null) {
            mainActivity.onDataFetched(xmlData);
            Log.d("XMLData", xmlData);
        } else {
            Log.e("Error", "Failed to fetch data from the server");
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
