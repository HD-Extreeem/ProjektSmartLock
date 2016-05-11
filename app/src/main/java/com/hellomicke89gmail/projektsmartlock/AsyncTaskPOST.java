package com.hellomicke89gmail.projektsmartlock;
import android.os.AsyncTask;
import android.util.Base64;
//import com.fasterxml.jackson.databind.*;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.core.JsonParseException;
import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
//import com.fasterxml
/**
 * Created by hello on 2016-04-20.
 */
public class AsyncTaskPOST extends AsyncTask<Void,Void,Void>{
    HashMap<String, Boolean> idMap;
    HashMap<String, String> idNameMap;
    String authString;

    AsyncTaskPOST(HashMap<String,Boolean > idMap, HashMap<String ,String> idNameMap, String authString){
        this.idMap=idMap;
        this.idNameMap=idNameMap;
        this.authString=authString;

    }

    @Override
    protected Void doInBackground(Void ...params) {
        JSONObject idNameMap=new JSONObject(this.idNameMap);
        JSONObject idMap=new JSONObject(this.idMap);
        JSONObject rfidMap=new JSONObject();


        try {

            rfidMap.put("rfidMap",idMap);
            rfidMap.put("idNameMap", idNameMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        URL url = null;
        try {
            HttpURLConnection urlConnection;

            byte[] data = Base64.decode(authString, Base64.DEFAULT);
            String authorize = new String(data);

            System.out.println("Base64 encoded auth string: " + authString);

            url = new URL("https://" + authorize + "@lockdroid.se/admin");
            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Authorization", "Basic " + authString);

            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(rfidMap.toString().length());

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type,","application/json; charset=UTF-8");
            OutputStreamWriter write=new OutputStreamWriter(urlConnection.getOutputStream());

            write.write(rfidMap.toString());
            write.flush();
            write.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }return null;
    }
}

