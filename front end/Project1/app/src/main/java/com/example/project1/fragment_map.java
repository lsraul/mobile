package com.example.project1;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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


public class fragment_map extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private int status;
    private int REQUEST_PERMISSION = 200;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);


        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        requests();

        if (status == ConnectionResult.SUCCESS) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if(mapFragment == null){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mapFragment = SupportMapFragment.newInstance();
                ft.replace(R.id.map, mapFragment).commit();
            }
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getContext(), 10);
            dialog.show();
        }

        return view;
    }

    /**
     * Initialization of the map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /**
         * Map settings
         */
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        /**
         * Gets the geolocalization
         */
        LocationManager locMan = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location loc = locMan.getLastKnownLocation(locMan.getBestProvider(new Criteria(), false));



        float zoomlevel = 16;


        new GooglePlaces().execute();
        LatLng Linna = new LatLng(63.11533383691571, 21.60925546569132);
        //LatLng myUbication = new LatLng(loc.getLatitude(), loc.getLongitude());


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Linna, zoomlevel));


    }

    /**
     * Asks for the permission if we don´t have
     */
    private void requests() {

        try{
            //Check realtime permission if run higher API 23
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                },REQUEST_PERMISSION);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * Read the places stored in a json and set as a place in the map
     */
    private class GooglePlaces extends AsyncTask<Void,Void, ArrayList<GooglePlace>> {

        /**
         * Reads the json and store the data in a ArrayList<GooglePLace>
         */
        private String data = "";
        @Override
        protected ArrayList<GooglePlace> doInBackground(Void... voids) {
            ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
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

        @Override
        protected void onPostExecute(ArrayList<GooglePlace> temp) {

            /**
             * Adds the mark in the map
             */
            for(int i =0 ;i <temp.size(); i++){

                LatLng place = new LatLng(Double.parseDouble(temp.get(i).getLatitude()),Double.parseDouble(temp.get(i).getLongitude()));

                if(temp.get(i).getType().contains("restaurant") ){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_1)));
                }else if(temp.get(i).getType().contains("supermarket")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_2)));
                }else if(temp.get(i).getType().contains("going_out")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_3)));
                }else if(temp.get(i).getType().contains("house")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_4)));
                }else if(temp.get(i).getType().contains("disco")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_5)));
                }else if(temp.get(i).getType().contains("nature")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_6)));
                }else if(temp.get(i).getType().contains("uni")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_7)));
                }else if(temp.get(i).getType().contains("second_hand")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_8)));
                }else if(temp.get(i).getType().contains("sight")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_9)));
                }else if(temp.get(i).getType().contains("sunset")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_10)));
                }else if(temp.get(i).getType().contains("coffee")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_11)));
                }else if(temp.get(i).getType().contains("shop")){
                    mMap.addMarker(new MarkerOptions().position(place).title(temp.get(i).getName()).snippet(temp.get(i).getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_12)));
            }
            }

        }


    }


}
