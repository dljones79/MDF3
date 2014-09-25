// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;


import android.app.Fragment;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MyMapFragment extends MapFragment implements GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    GoogleMap mMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mMap = getMap();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
