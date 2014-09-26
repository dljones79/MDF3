// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class FormActivity extends Activity {

    public Double mLatitude = null;
    public Double mLongitude = null;
    public static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        if (bundle != null){
            mLatitude = bundle.getDouble("lat");
            mLongitude = bundle.getDouble("lon");
        }

        if (mLatitude != null){
            Bundle args = new Bundle();
            args.putDouble("lat", mLatitude);
            args.putDouble("lon", mLongitude);

            FormFragment formFragment = FormFragment.newInstance();
            formFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.formContainer, formFragment).commit();
        } else {
            FormFragment formFragment = FormFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.formContainer, formFragment).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
