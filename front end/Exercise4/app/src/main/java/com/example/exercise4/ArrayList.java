package com.example.exercise4;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ArrayList<GooglePlace> extends AsyncTask<Void,Void,Void> {




    @Override
    protected Void doInBackground(Void... voids) {
        java.util.ArrayList<com.example.exercise4.GooglePlace> temp = new java.util.ArrayList<com.example.exercise4.GooglePlace>();
        String data ="";

        try {
            URL url = new URL("https://firebasestorage.googleapis.com/v0/b/project1-1caff.appspot.com/o/places.json?alt=media&token=1f6e1951-6f4b-461d-ba4d-8ed4b8d8c998");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for(int i =0 ;i <JA.length(); i++){

                JSONObject JO = (JSONObject) JA.get(i);
                com.example.exercise4.GooglePlace place = new com.example.exercise4.GooglePlace((String)JO.get("id"), (String)JO.get("name"), (String)JO.get("latitude"), (String)JO.get("longitude"), (String)JO.get("description"), (String)JO.get("type"));
                temp.add(i, place);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return temp;
        return null;
    }
}
