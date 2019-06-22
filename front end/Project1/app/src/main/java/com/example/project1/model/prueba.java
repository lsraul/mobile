package com.example.project1.model;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.project1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class prueba extends AsyncTask<String, Void, String> {

    public static String texto="";
    private double latFrom;
    private double latTo;
    private double lngFrom;
    private double lngTo;

    @Override
    protected String doInBackground(String... strings) {

        latFrom=63.11147512251782;
        lngFrom=21.603103497305284;
        latTo=63.10378111708289;
        lngTo=21.623749452749507;
        try {
            URL url=new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyBVZ9jPtjPpnDWwPYzKNTCYnQP8zx5niJ0");
            //URL url=new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latFrom + "," + lngFrom + "&destination=" + latTo + "," + lngTo + "&mode=driving&sensor=false");
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode=con.getResponseCode();
            if(statuscode==HttpURLConnection.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=br.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line=br.readLine();
                }
                String json=sb.toString();

                Log.d("JSON",json);
                JSONObject root=new JSONObject(json);
                JSONArray array_rows=root.getJSONArray("rows");
                Log.d("JSON","array_rows:"+array_rows);
                JSONObject object_rows=array_rows.getJSONObject(0);
                Log.d("JSON","object_rows:"+object_rows);
                JSONArray array_elements=object_rows.getJSONArray("elements");
                Log.d("JSON","array_elements:"+array_elements);
                JSONObject  object_elements=array_elements.getJSONObject(0);
                Log.d("JSON","object_elements:"+object_elements);
                JSONObject object_duration=object_elements.getJSONObject("duration");
                JSONObject object_distance=object_elements.getJSONObject("distance");
                Log.d("JSON","object_duration:"+object_duration);
                texto = object_duration.getString("value")+","+object_distance.getString("value");
                return object_duration.getString("value")+","+object_distance.getString("value");


/*
                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray array = jsonObject.getJSONArray("routes");
                JSONObject routes = array.getJSONObject(0);
                JSONArray legs = routes.getJSONArray("legs");
                JSONObject steps = legs.getJSONObject(0);
                JSONObject distance = steps.getJSONObject("distance");

                Log.d("Distance", distance.toString());
                texto = distance.getString("text").replaceAll("[^\\.0123456789]","") ;
                return texto;
*/


            }
        } catch (MalformedURLException e) {
            Log.d("error", "error1");
        } catch (IOException e) {
            Log.d("error", "error2");
        } catch (JSONException e) {
            Log.d("error","error3");
        }


        return null;
    }
}
