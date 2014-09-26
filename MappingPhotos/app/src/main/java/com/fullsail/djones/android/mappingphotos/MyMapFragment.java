// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MyMapFragment extends MapFragment implements GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, LocationListener {

    GoogleMap mMap;
    ArrayList<DataObject> locations;
    LocationManager mManager;
    Double mLatitude;
    Double mLongitude;
    CameraPosition cameraPosition;
    DataObject location;

    private static final int REQUEST_ENABLE_GPS = 0x02101;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Try to load data from storage
        loadData();
        mMap = getMap();

        mManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        enableGps();
        mMap.setMyLocationEnabled(true);

        Location currentLocation = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng coord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        /*
        CameraUpdate thisLocation = CameraUpdateFactory.newLatLngZoom(coord, 7);
        mMap.animateCamera(thisLocation);
        */
        cameraPosition = new CameraPosition.Builder()
                .target(coord)
                .zoom(7)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        if (locations != null) {
            for (DataObject e : locations) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude())).title(e.getName()));
            }

            mMap.setInfoWindowAdapter(new MarkerAdapter());
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setMyLocationEnabled(true);
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatitude, mLongitude), 16));
            if (cameraPosition != null) {
                mMap.animateCamera(newCameraPosition(cameraPosition));
            }
        }
    }

    private void enableGps() {
        if(mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location loc = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) {
                mLatitude = loc.getLatitude();
                mLongitude = loc.getLongitude();
            }

        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Unavailable")
                    .setMessage("Please enable GPS in the system settings.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                        }

                    })
                    .show();
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {

        Log.i("Locations", locations.toString());

        for (int i = 0; i < locations.size(); i++){
            location = new DataObject();
            location = locations.get(i);
            if (location.getName().matches(marker.getTitle())){
                Log.i("Match:", "It Matches!");
                Intent intent = new Intent(getActivity(), ViewActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            } else {
                Log.i("Match:", "No Match!");
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onPause(){
        super.onPause();

        mManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableGps();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
