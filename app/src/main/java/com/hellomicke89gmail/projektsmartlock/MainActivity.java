package com.hellomicke89gmail.projektsmartlock;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView loginview;
    private EditText loginedit;
    private Button btnlogin;
    private TextView errorText;
    //  private TextView passwordview;
    private EditText passwordedit;
    private String pass;
    private String name;
    private int selected = 0;
    private String passname = "";

    private boolean clicked = false;
    private int attemptCounter = 3;
    private static String result;
    private ApprovedListView approvedListView = new ApprovedListView();

    private String authString="";

    private static TextInputLayout password;
    private static TextInputLayout username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newactivity);
        username = (TextInputLayout) findViewById(R.id.user);
        password = (TextInputLayout) findViewById(R.id.passsword);


        addListenerOnButton();
        loginedit.setText("some user");
        passwordedit.setText("some pass");
        //username.setErrorEnabled(false);
        //password.setErrorEnabled(false);

    }




    public void addListenerOnButton() {
        View.OnClickListener choiceListener = new ChoiceButtonListener();
        btnlogin = (Button) findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(choiceListener);
        loginedit = (EditText) findViewById(R.id.editText);


        passwordedit = (EditText) findViewById(R.id.editText2);




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

        name = loginedit.getText().toString();
        pass = passwordedit.getText().toString();

        passname = name + ":" + pass;
        MyTask2 task = new MyTask2();

        task.execute();

    }


    class MyTask2 extends AsyncTask<String, Void, Integer> {

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
                // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //OutputStreamWriter out2 = new OutputStreamWriter(urlConnection.getOutputStream());
                //out2.write("open");
                //out2.flush();
                //out2.close();
                //in.close();
            }
            else if(result>=500){
                errorServer();
            }
            else{

                error();
            }
        }
    }

    private void Approved(){


        approvedListView.setCredentials(authString,name);

        Intent i = new Intent(MainActivity.this, ApprovedListView.class);
        startActivity(i);
        btnlogin.setEnabled(true);

    }
    private void error(){

        username.setError("Username Is Wrong, Try Again!");
        password.setError("Password Is Wrong, Try Again!");
    }
    private void errorServer(){
        username.setError("Server is currently down, Try again later!");
        password.setError("Server is currently down, Try again later!");
    }
}

