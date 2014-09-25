// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;


import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MyMapFragment extends MapFragment implements GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    GoogleMap mMap;
    ArrayList<DataObject> locations;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        loadData();

        mMap = getMap();
        if (locations != null) {
            for (DataObject e : locations) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude())).title(e.getName()));
            }

            mMap.setInfoWindowAdapter(new MarkerAdapter());
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.593770, -81.303797), 16));
        }
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    // Custom InfoWindowAdapter Class
    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {
        TextView mText;

        public MarkerAdapter() { mText = new TextView(getActivity()); }

        @Override
        public View getInfoContents(Marker marker) {
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) { return null; }
    }

    // Load data from file
    public void loadData(){
        try{
            FileInputStream fin = getActivity().openFileInput("data.txt");
            ObjectInputStream oin = new ObjectInputStream(fin);
            int count = oin.readInt();
            locations = new ArrayList<DataObject>();
            for (int i = 0; i < count; i++)
                locations.add((DataObject) oin.readObject());
            oin.close();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
