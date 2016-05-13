package com.hellomicke89gmail.projektsmartlock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    private EditText loginedit;
    private Button btnlogin;
    private EditText passwordedit;
    private String pass;
    private String name;
    private String passname = "";
    private MainActivity mainActivity = new MainActivity();

    private String authString="";

    private static TextInputLayout password;
    private static TextInputLayout username;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        registercomponents();
        addListenerOnButton();

        loginedit.setText("");
        passwordedit.setText("");
        //username.setErrorEnabled(false);
        //password.setErrorEnabled(false);

    }




    public void addListenerOnButton() {
        View.OnClickListener choiceListener = new ChoiceButtonListener();
        btnlogin.setOnClickListener(choiceListener);
    }

    private void registercomponents() {
        loginedit = (EditText) findViewById(R.id.editText);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        username = (TextInputLayout) findViewById(R.id.user);
        password = (TextInputLayout) findViewById(R.id.passsword);
        passwordedit = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }


    private class ChoiceButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            if (v.getId() == R.id.btnlogin) {
                btnlogin.setEnabled(false);
                start();
            }
        }
    }

    private void start() {
        progressBar.setVisibility(View.VISIBLE);

        name = loginedit.getText().toString();
        pass = passwordedit.getText().toString();

        passname = name + ":" + pass;
        MyTask2 task = new MyTask2();

        task.execute();

    }


    class MyTask2 extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }

        protected Integer doInBackground(String... params) {
            HttpsURLConnection urlConnection;
            int statusCode=0;
            String statusmessage="";

            try {

                byte[] authEncBytes = Base64.encode(passname.getBytes(), Base64.DEFAULT);

                authString = new String(authEncBytes);

                System.out.println("Base64 encoded auth string: " + authString);

                URL url = new URL("https://" + passname + "@lockdroid.se/login");

                urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Authorization", "Basic " + authString);



                statusCode = urlConnection.getResponseCode();
                statusmessage = urlConnection.getResponseMessage();


                System.out.println(passname);
                System.out.println(url);
                System.out.println(statusCode);
                System.out.println(statusmessage);


            } catch (Exception ex) {
                System.out.println(ex);
            }
            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer result) {


            if(result == 200) {
                Approved();
            }
            else if(result>=500){
                errorServer();
            }
            else{
                error();
            }
        }
    }

    private void Approved() {



        mainActivity.setCredentials(authString,name);

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        btnlogin.setEnabled(true);
        progressBar.setVisibility(View.GONE);

    }
    private void error(){

        username.setError("Username Is Wrong, Try Again!");
        password.setError("Password Is Wrong, Try Again!");
        progressBar.setVisibility(View.GONE);
    }
    private void errorServer(){
        username.setError("Server is currently down, Try again later!");
        password.setError("Server is currently down, Try again later!");
        progressBar.setVisibility(View.GONE);
    }
}

