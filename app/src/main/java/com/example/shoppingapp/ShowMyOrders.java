package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.recyclerviewadapter.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ShowMyOrders extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private  ArrayList<JSONObject> productArrayList;
    private ArrayAdapter<String> arrayAdapter;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_orders);

        Intent intent = getIntent();
        try {
            jsonArray = new JSONArray(intent.getStringExtra("Product Details"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            productArrayList.add(jsonObject);

            Log.i("info", jsonObject.toString());
        }
        recyclerViewAdapter = new RecyclerViewAdapter(ShowMyOrders.this, productArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


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

