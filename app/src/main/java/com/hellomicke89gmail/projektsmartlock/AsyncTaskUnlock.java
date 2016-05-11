package com.hellomicke89gmail.projektsmartlock;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by HadiDeknache on 16-04-28.
 */
public class AsyncTaskUnlock extends  AsyncTask<Void,Void,Void>{
    String authString;


    public AsyncTaskUnlock (String authString){
        this.authString=authString;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpsURLConnection urlConnection;

        try {
            byte[] data = Base64.decode(authString, Base64.DEFAULT);
            String authorize = new String(data);

            System.out.println("Base64 encoded auth string: " + authString);

            URL url = new URL("https://" + authorize + "@lockdroid.se/client?message=open");
            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Authorization", "Basic " + authString);

            int statusCode = urlConnection.getResponseCode();
            System.out.println(authorize);

            System.out.println(url);

            System.out.println(statusCode);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

}
