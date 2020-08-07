package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class ViewObjectsActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_objects);
        addViewPager();
    }
    private void addViewPager() {

        //This adds the viewPager through which the top image slides and changes.
        viewPager2 = findViewById(R.id.viewPager2);
        int[] array = {R.drawable.aone, R.drawable.atwo, R.drawable.athree, R.drawable.afour};
        //The code from here shuffles the images and randomizes them

        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
        //finally adds view pager
        viewPager2.setAdapter(new ViewPagerAdapter(this, array));

    }

    public void newActivity(View view) {
        //for taking user to second activity
        Intent intent = new Intent(getApplicationContext(), ScreenTwo.class);
        startActivity(intent);
    }
    public void viewOrders(View view) {GetAPI api = new GetAPI();
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