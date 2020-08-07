package com.example.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    String finalPassword = "";
    SharedPreferences sharedPreferences;
    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!sharedPreferences.getString("emailId", "empty").equals("empty") && !sharedPreferences.getString("password", "empty").equals("empty")){
            MainActivity.EmailAddress = sharedPreferences.getString("emailId","empty");
            finalPassword = sharedPreferences.getString("password","empty");
            tryLoginUser();
        }

        initViews();
        setListeners();
        //try auto-login

        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        emailid = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        signUp = (TextView) view.findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        MainActivity.EmailAddress = emailid.getText().toString();
        finalPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(MainActivity.EmailAddress);

        // Check for both field is empty or not
        if (MainActivity.EmailAddress.equals("") || MainActivity.EmailAddress.length() == 0
                || finalPassword.equals("") || finalPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else
            tryLoginUser();

    }

    private void tryLoginUser() {

        getAPI api = new getAPI();
        try {
            final String result = api.execute("http://3.16.109.253:8080/login").get();
            Log.i("info",result);
            if(result.equals("InvalidUser")){
                //Password/email wrong
                new CustomToast().Show_Toast(getActivity(), view,
                        "The Password is Incorrect.");
            }else if(result.equals("errorA")){
                //error happened.
                new CustomToast().Show_Toast(getActivity(), view,
                        "This Email ID has not been registered. ");
            }else if(result.equals("error")){
                //error happened.
                new CustomToast().Show_Toast(getActivity(), view,
                        "There was some Error! Try again Later! ");
            }else if(result.equals("exceptionerror")){
                new CustomToast().Show_Toast(getActivity(), view,
                        "There was some Error! Check your internet connection or Try again later! ");
            }
            else{
                if((sharedPreferences.getString("emailId", "empty").equals("empty") && sharedPreferences.getString("password", "empty").equals("empty")) || (!sharedPreferences.getString("emailId", "empty").equals(MainActivity.EmailAddress)&& !sharedPreferences.getString("password", "empty").equals(finalPassword))){
                    new AlertDialog.Builder(getActivity())
                            .setIcon(R.drawable.confirm_password)
                            .setTitle("Save Password?")
                            .setMessage("Would you like to save this account for Auto-Login in future?")
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    sharedPreferences.edit().putString("emailId",MainActivity.EmailAddress).apply();
                                    sharedPreferences.edit().putString("password",finalPassword).apply();
                                    MainActivity.token = result;
                                    Log.i("info",MainActivity.token);
                                    Intent intent = new Intent(getContext(),ViewObjectsActivity.class);
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.token = result;
                                    Log.i("info",MainActivity.token);
                                    Intent intent = new Intent(getContext(),ViewObjectsActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }else{
                    MainActivity.token = result;
                    Log.i("info",MainActivity.token);
                    Intent intent = new Intent(getContext(),ViewObjectsActivity.class);
                    startActivity(intent);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    public class getAPI extends AsyncTask<String,Void, String> {
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

                String input = "{\"email\":\""+MainActivity.EmailAddress+"\",\"password\":\""+finalPassword+"\"}";
                Log.i("info",input);

                conn.connect();
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
                Log.i("info", String.valueOf(conn.getResponseCode()));
                if(conn.getResponseCode()==404){
                    return "errorA";
                }
                else if (conn.getResponseCode() != 200) {
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
                return "exceptionerror";
            }
        }
    }
}