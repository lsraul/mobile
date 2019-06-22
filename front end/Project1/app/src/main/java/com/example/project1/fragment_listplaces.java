package com.example.project1;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.model.prueba;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class fragment_listplaces extends Fragment {

    /**
     * Declaration of the variables that we are going to use
     */
    private Spinner options;
    private ListView places;
    private ArrayAdapter<CharSequence> adapter1;
    static ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
    private int type;
    private int ALL = 0;
    private int RESTAURANT = 1;
    private int SUPERMARKET = 2;
    private int GOING_OUT = 3;
    private int HOUSE = 4;
    private int NATURE = 5;
    private int UNI = 6;
    private int SECOND_HAND = 7;
    private int SIGHT = 8;
    private int COFFEE = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Inflate the layout for this segment
         */
        View view = inflater.inflate(R.layout.fragment_listplaces, container, false);
        places = (ListView) view.findViewById(R.id.places);

        /**
         *Initialization and configuration of the spinner.
         * With this spinner the user can choose which type of placelist it will be shown: restaurants, supermarkets, ...
         */
        options = (Spinner) view.findViewById(R.id.spinner_places);
        adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.options, android.R.layout.simple_spinner_dropdown_item);
        options.setAdapter(adapter1);

        options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=position;
                new GooglePlaces().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    /**
     * GooglePlaces read the information of the places stored in a json in a firestorage and send the arraylist to AdapterPlaces
     * with the type of places selected by the spinner.
     */
    private class GooglePlaces extends AsyncTask<Void,Void, ArrayList<GooglePlace>> {

        private String data = "";
        @Override
        protected ArrayList<GooglePlace> doInBackground(Void... voids) {
            temp.clear();
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
                    GooglePlace place = new GooglePlace((String)JO.get("id"), (String)JO.get("name"), (String)JO.get("latitude"), (String)JO.get("longitude"), (String)JO.get("description"), (String)JO.get("description_short"), (String)JO.get("type"));
                    if(type==ALL){
                        temp.add(place);
                    }else if(type==RESTAURANT && place.getType().contains("restaurant") ){
                        temp.add(place);
                    }else if(type==SUPERMARKET && place.getType().contains("supermarket")){
                        temp.add(place);
                    }else if(type==GOING_OUT && (  place.getType().contains("going_out") || place.getType().contains("disco")  ) ){
                        temp.add(place);
                    }else if(type==HOUSE && place.getType().contains("house")){
                        temp.add(place);
                    }else if(type==NATURE && (  place.getType().contains("nature") || place.getType().contains("sunset")  )   ){
                        temp.add(place);
                    }else if(type==UNI && place.getType().contains("uni")){
                        temp.add(place);
                    }else if(type==SECOND_HAND &&  (  place.getType().contains("second_hand") || place.getType().contains("shop") )  ){
                        temp.add(place);
                    }else if(type==SIGHT && place.getType().contains("sight")){
                        temp.add(place);
                    }else if(type==COFFEE && place.getType().contains("coffee")){
                        temp.add(place);
                    }

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

        @Override
        protected void onPostExecute(ArrayList<GooglePlace> temp) {

            places.setAdapter(new AdapterPlaces(temp));

        }
    }
}
