package com.example.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterPlaces extends BaseAdapter {

    private static LayoutInflater inflater = null;

    //Context context;
    private ArrayList<GooglePlace> temp;
    /**
     * The constructor
     * @param temp
     */
    public AdapterPlaces( ArrayList<GooglePlace> temp) {//
        //super(context, R.layout.places_row, temp);
        //this.context = conext;
        this.temp = temp;
        //Cada vez que cambiamos los elementos debemos noficarlo
        notifyDataSetChanged();
        //inflater = (LayoutInflater)conext.getSystemService(conext.LAYOUT_INFLATER_SERVICE);
    }
    /**
     * This method simply returns the number of
     * Elements of our ListView. Obviously it is the size of the arraylist
     */
    @Override
    public int getCount() {
        return temp.size();
    }
    /**
     * This method return the element of the required position
     */
    @Override
    public Object getItem(int position) {
        return temp.get(position);
    }
    /**
     * Return the element ID. Its not used so it returns 0.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }
    /**
     * The most complicated method. Here we have to return the View to represent.
     * In this method we pass 3 values. The first is the position of the element,
     * the second one is the View to be used that will be one that is no longer visible and that
     * we will reuse it, and the last one is the ViewGroup, in our case, the ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * The first thing we will do is check if the View already exists and we have to recycle it
         * or on the contrary we must create a new one.
         */

        PlacesView view;
        if (convertView == null) //NO existe, creamos uno
            view = new PlacesView(parent.getContext());
        else                    //Existe, reutilizamos
            view = (PlacesView) convertView;



        view.setGooglePlace(temp.get(position));

        return view;


    }


    private class PlacesView extends LinearLayout {

        private TextView name;
        private TextView description;


        public PlacesView(Context context) {
            super(context);
            inflate(context, R.layout.places_row, this);

            /**
             * It is very important to save the addresses of the elements
             * that we are going to change as the findViewById requires a lot of time
             * and if every time we scroll we have to look for all the elements
             * We will unnecessarily load the terminal and lose fluidity
             */

            name        = (TextView) findViewById(R.id.placeName);
            description        = (TextView) findViewById(R.id.placeDescription);

        }

        /**
         * This method will allow us to assign the values to the different
         * graphic components according to the object that we want to see.
         * @param googlePlace
         */
        public void setGooglePlace(GooglePlace googlePlace) {
            name.setText(""+googlePlace.getName());
            description.setText(""+googlePlace.getDescription());
        }

    }

}