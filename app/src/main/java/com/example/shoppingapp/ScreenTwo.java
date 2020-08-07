package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScreenTwo extends AppCompatActivity {

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    static String laptopname;
    static String companyname;
    static String productname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_two);
        text1 = findViewById(R.id.textView2);
        text2 = findViewById(R.id.textView4);
        text3 = findViewById(R.id.textView5);
        text4 = findViewById(R.id.textView6);
        text5 = findViewById(R.id.textView00);


    }


    public void makeTextBold(View view) {
        //Whichever category of products is chosen, it turns black(Highlighted) and others become grey
        TextView textView = (TextView) view;
        textView.setTextColor(Color.BLACK);

        if(textView.getId()!=text1.getId()){
            text1.setTextColor(Color.GRAY);
        }
        if(textView.getId()!=text2.getId()){
            text2.setTextColor(Color.GRAY);
        }
        if(textView.getId()!=text3.getId()){
            text3.setTextColor(Color.GRAY);
        }
        if(textView.getId()!=text4.getId()){
            text4.setTextColor(Color.GRAY);
        }
        if(textView.getId()!=text5.getId()){
            text5.setTextColor(Color.GRAY);
        }

    }

    public void newActivity(View view) {
        //for taking to last activity (check out screen) whenever any of the products are chosen.
        //Also to store product name so that it can be displayed appropriately on check out screen.
        TextView textView;
        ImageView imageView = (ImageView)view;
        int tag = Integer.parseInt(String.valueOf(imageView.getTag()));
        if (tag == 1) {
            textView = findViewById(R.id.a);
            productname = (String) textView.getText();
        } else if (tag == 2) {
            textView = findViewById(R.id.b);
            productname = (String) textView.getText();
        } else if (tag == 3) {
            textView = findViewById(R.id.c);
            productname = (String) textView.getText();
        } else if (tag == 4) {
            textView = findViewById(R.id.d);
            productname = (String) textView.getText();
        } else if (tag == 5) {
            textView = findViewById(R.id.e);
            productname = (String) textView.getText();
        } else if (tag == 6) {
            textView = findViewById(R.id.f);
            productname = (String) textView.getText();
        } else if (tag == 7) {
            textView = findViewById(R.id.g);
            productname = (String) textView.getText();
        } else if (tag == 8) {
            textView = findViewById(R.id.h);
            productname = (String) textView.getText();
        }else {
            productname = "Asus Zephrus Gaming Laptop";
        }
        String[] brokenproduct = productname.split(" ", 2);
        companyname = brokenproduct[0];
        laptopname = brokenproduct[1];
        Intent intent = new Intent(getApplicationContext(),LastScreen.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        //Returns user to last activity when back arrow is clicked.
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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