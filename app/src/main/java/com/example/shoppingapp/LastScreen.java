package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class LastScreen extends AppCompatActivity {
    ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_screen);
        TextView companynametext = findViewById(R.id.companyname);
        TextView laptopnametext = findViewById(R.id.laptopname);
        companynametext.setText(ScreenTwo.companyname);
        laptopnametext.setText(ScreenTwo.laptopname);
        addViewPager();


    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView companynametext = findViewById(R.id.companyname);
        TextView laptopnametext = findViewById(R.id.laptopname);
        companynametext.setText(ScreenTwo.companyname);
        laptopnametext.setText(ScreenTwo.laptopname);
    }

    private void addViewPager() {
        //add view pager to have a swipable image on top.
        int[] array = {R.drawable.hplight, R.drawable.razer, R.drawable.predator, R.drawable.macairoriginal};
        //The code from here shuffles the images and randomizes them

        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
        //Adds the view pager
        viewPager2 = findViewById(R.id.viewPager3);
        viewPager2.setAdapter(new ViewPagerAdapter(this, array));
    }

    public void returntoactivity(View view) {
        Intent back = new Intent(getApplicationContext(), ScreenTwo.class);
        startActivity(back);
    }

    public void Tick(View view) {
        API api = new API();
        try {
            String result = api.execute("http://3.16.109.253:8080/addProduct").get();
            //Log.i("info",result);
            if (result.equals("success")) {
                final ImageView imageView = findViewById(R.id.tick);
                imageView.setVisibility(VISIBLE);

                ((Animatable) imageView.getDrawable()).start();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        imageView.setVisibility(INVISIBLE);
                    }
                }, 1000);
            } else if (result.equals("403")) {
                Log.i("info", "forbidden");
                //error happened.
                Toast.makeText(this,"Error! Login Again.",Toast.LENGTH_SHORT).show();
            } else {
                Log.i("info", result);
                Toast.makeText(this,"Error! Check Your Internet Connection.",Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void viewOrders(View view) {
        GetAPI api = new GetAPI();
        try {
            String result = api.execute("http://3.16.109.253:8080/viewOrder/" + MainActivity.EmailAddress + "/token/" + MainActivity.token).get();
            if (result.equals("403")) {
                //Password/email wrong
                Toast.makeText(this,"Error! Login Again.",Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                //error happened.
                Toast.makeText(this,"Error! Check your internet connection.",Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Log.i("info", jsonObject.toString());
                }
                Intent intent = new Intent(getApplicationContext(),ShowMyOrders.class);
                intent.putExtra("Product Details",result);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Error! Try Again Later.",Toast.LENGTH_SHORT).show();
        }
    }

    public class API extends AsyncTask<String, Void, String> {
        URL url;
        HttpURLConnection conn = null;

        @Override
        protected String doInBackground(String... strings) {
            try {

                url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                String input = "{\"email\":\"" + MainActivity.EmailAddress + "\",\"token\":\"" + MainActivity.token + "\",\"productName\":\"" + ScreenTwo.productname + "\"}";
                Log.i("info", input);

                conn.connect();
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                Log.i("info", conn.getResponseCode() + " " + conn.getResponseMessage());

                if (conn.getResponseCode() == 403) {
                    return "403";
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
    public void logout(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("emailId","empty").apply();
        sharedPreferences.edit().putString("password","empty").apply();
        MainActivity.EmailAddress = "";
        MainActivity.token = "";
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
