package com.hellomicke89gmail.projektsmartlock;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hello on 2016-05-04.
 */
public class AsycTaskPostToken extends AsyncTask<Void,Void,Void>{
        private String token;
        private String authString;

    AsycTaskPostToken(String token,String authString){
            this.token=token;
            this.authString=authString;

        }

        @Override
        protected Void doInBackground(Void ...params) {
            URL url = null;
            Log.v("AsycTaskPostToken", "DO IN BACKGROUND");
            try {
                HttpURLConnection urlConnection;


                System.out.println("Base64 encoded auth string: " + authString);

                url = new URL("https://" + authString + "@lockdroid.se/pushtokens");
                urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Authorization", "Basic " + authString);

                urlConnection.setDoOutput(true);
                urlConnection.setFixedLengthStreamingMode(token.length());

                urlConnection.setRequestMethod("POST");

              //  int statusCode = urlConnection.getResponseCode();

              //  if (statusCode==200){
                    OutputStreamWriter write=new OutputStreamWriter(urlConnection.getOutputStream());
                    write.write(token);
                    write.flush();
                    write.close();
                    urlConnection.disconnect();
                    Log.v("AsycTaskPostToken", "TOKEN SENT TO SERVER");
              //  }else{
              //  }

          //      urlConnection.setRequestProperty("Content-Type,","application/json; charset=UTF-8");

            } catch (MalformedURLException e) {
                Log.v("AsycTaskPostToken", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.v("AsycTaskPostToken", e.toString());
                e.printStackTrace();
            }return null;
        }
}