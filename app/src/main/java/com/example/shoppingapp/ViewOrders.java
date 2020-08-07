package com.example.shoppingapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetAPI extends AsyncTask<String, Void, String> {
        URL url;
        HttpURLConnection conn = null;

        @Override
        protected String doInBackground(String... strings) {
            try {

                url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                Log.i("info",conn.getResponseCode()+" "+conn.getResponseMessage());
                if (conn.getResponseCode() == 403) {
                    return "InvalidUser";
                }else if (conn.getResponseCode() != 200){
                    return "error";
                }

                InputStreamReader inputStreamReader = new InputStreamReader(
                        (conn.getInputStream()));

                String result = "";
                int data = inputStreamReader.read();
                char current;
                while (data != -1) {
                    current = (char) data;
                    result += current;
                    data = inputStreamReader.read();

                }
                conn.disconnect();
                return result;


            } catch (IOException e) {

                e.printStackTrace();

                return "error";
            }
        }
    }

