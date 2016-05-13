package com.hellomicke89gmail.projektsmartlock;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class AsyncTaskGET extends AsyncTask<String,Void,Integer>{
        private static final String TAG = "RecyclerViewExample";
        MainActivity mainActivity;
        ArrayList<Person> keys = new ArrayList<>();
        HashMap<String, String> idNameMap=new HashMap<>();
        HashMap<String ,Boolean> idMap= new HashMap<>();
        String authString;

        idFragment fragment;



        AsyncTaskGET(MainActivity mainActivity, String authString){
            this.mainActivity = mainActivity;
            this.authString=authString;

        }
        @Override
        protected  Integer doInBackground(String... params){

            Integer result =0;
            HttpURLConnection urlConnection;
            try{

                System.out.println("Base64 encoded auth string: " + authString);

                URL url = new URL("https://" + authString + "@lockdroid.se/admin");
                urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Authorization", "Basic " + authString);
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                if (statusCode==200){
                    BufferedReader read = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = read.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result =1;
                }
                else{
                    //urlConnection.disconnect();
                    result =0;
                }

            }catch (Exception e){
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }
    @Override
    protected void onPostExecute (Integer result){

        if (result==1){

            mainActivity.updateIdAdapter(idMap, idNameMap);

        }
        else {
            mainActivity.errorToast("Failed to get id" );

        }
    }


    private void parseResult(String result){
        try{
            idMap.clear();

            JSONObject response = new JSONObject(result);
            JSONObject rfidpost = response.optJSONObject("rfidMap");


            Iterator<?> keys=rfidpost.keys();


            while(keys.hasNext()){
                String key= (String) keys.next();
                Boolean value=rfidpost.getBoolean(key);

                idMap.put(key,value);

            }
            JSONObject idNamepost=response.optJSONObject("idNameMap");
            Iterator<?> idname=idNamepost.keys();
            while(idname.hasNext()){
                String key= (String) idname.next();
                String value=idNamepost.getString(key);

                idNameMap.put(key,value);

            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }


}
