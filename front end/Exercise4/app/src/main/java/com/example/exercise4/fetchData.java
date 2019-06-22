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
import java.util.ArrayList;


public class fetchData extends AsyncTask<Void,Void,Void> {
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();

    @Override
    protected Void doInBackground(Void... voids) {

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
            //GooglePlace places = new GooglePlace(0,"","","","",0);
             for(int i =0 ;i <JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
                String id = (String)JO.get("id");
                 GooglePlace place = new GooglePlace((String)JO.get("id"), (String)JO.get("name"), (String)JO.get("latitude"), (String)JO.get("longitude"), (String)JO.get("description"), (String)JO.get("type"));
                //(String)JO.get("id")
                 //(String)JO.get("type")
                 temp.add(i, place);

                singleParsed =  "ID:" + temp.get(i).getId() + "\n"+
                        "Name:" + temp.get(i).getName() + "\n"+
                        "Latitude:" + temp.get(i).getLatitude() + "\n"+
                        "Longitude:" + temp.get(i).getLongitude() + "\n"+
                         "Description:" + temp.get(i).getDescription() + "\n"+
                         "Type:" + temp.get(i).getType() + "\n";

                dataParsed = dataParsed + singleParsed +"\n" ;

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //MainActivity.data.setText(this.dataParsed);

    }

    public static ArrayList<GooglePlace> GooglePlaces(){
        ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
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
                GooglePlace place = new GooglePlace((String)JO.get("id"), (String)JO.get("name"), (String)JO.get("latitude"), (String)JO.get("longitude"), (String)JO.get("description"), (String)JO.get("type"));
                temp.add(i, place);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp;
    }









   /*
   @Override
    protected Void doInBackground(Void... voids) {
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
                singleParsed =  "ID:" + JO.get("id") + "\n"+
                        "Name:" + JO.get("name") + "\n"+
                        "Latitude:" + JO.get("latitude") + "\n"+
                        "Longitude:" + JO.get("longitude") + "\n"+
                         "Description:" + JO.get("description") + "\n"+
                         "Type:" + JO.get("type") + "\n";

                dataParsed = dataParsed + singleParsed +"\n" ;


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    */
}

