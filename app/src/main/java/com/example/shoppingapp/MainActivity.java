package com.example.shoppingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction ft;
    public static String token;
    public static String EmailAddress;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = "";
        EmailAddress = "";
// If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {

            ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frameContainer, new Login_Fragment());
            ft.commit();

        }

//Onclick sluiticoon om af te sluiten
        findViewById(R.id.close_activity).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();

                    }
                });

    }

    // Replace Login Fragment with animation
    protected void replaceLoginFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.left_enter, R.anim.right_out);
        ft.replace(R.id.frameContainer, new Login_Fragment(), Utils.Login_Fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {

// Find the tag of signup and forgot password fragment
        Fragment Registerer_Fragment = getSupportFragmentManager()
                .findFragmentByTag(Utils.SignUp_Fragment);


// Check if both are null or not
// If both are not null then replace login fragment else do backpressed
// task

        if (Registerer_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }

}