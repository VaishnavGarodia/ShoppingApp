package com.example.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static FragmentManager fragmentManager2;
    String getFullName;
    String getMobileNumber;
    String getLocation;
    String getPassword;
    String getConfirmPassword;
    SharedPreferences sharedPreferences;

    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initViews();
        setListeners();
        fragmentManager2 = getActivity().getSupportFragmentManager();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        location = (EditText) view.findViewById(R.id.location);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                fragmentManager2
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new Login_Fragment(),
                                Utils.Login_Fragment).commit();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        getFullName = fullName.getText().toString();
        MainActivity.EmailAddress = emailId.getText().toString();
        getMobileNumber = mobileNumber.getText().toString();
        getLocation = location.getText().toString();
        getPassword = password.getText().toString();
        getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(MainActivity.EmailAddress);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || MainActivity.EmailAddress.equals("") || MainActivity.EmailAddress.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else
            signUpUser();

    }

    private void signUpUser() {
        getAPI api = new getAPI();
        try {
            final String result = api.execute("http://3.16.109.253:8080/addUser").get();
            Log.i("info", result);

            if(result.equals("error")){
                //error happened.
                new CustomToast().Show_Toast(getActivity(), view,
                        "There was some Error! Try again Later! ");
            }else if(result.equals("exceptionerror")){
                new CustomToast().Show_Toast(getActivity(), view,
                        "There was some Error! Check your internet connection or Try again later! ");
            }else {
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.confirm_password)
                        .setTitle("Save Password?")
                        .setMessage("Would you like to save this account information for Auto-Login in future?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sharedPreferences.edit().putString("emailId",MainActivity.EmailAddress).apply();
                                sharedPreferences.edit().putString("password",getPassword).apply();
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
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class getAPI extends AsyncTask<String, Void, String> {
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

                String input = "{\"email\":\"" + MainActivity.EmailAddress + "\",\"password\":\"" + getPassword + "\",\"address\":\"" + getLocation + "\",\"phone\":\"" + getMobileNumber + "\",\"name\":\"" + getFullName + "\"}";
                Log.i("info", input);

                conn.connect();
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                Log.i("info", String.valueOf(conn.getResponseCode()));
                if (conn.getResponseCode() != 200) {
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