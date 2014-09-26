// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ViewActivity extends Activity {

    public DataObject dataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        dataObject = new DataObject();

        Intent intent = getIntent();
        if (intent != null){
            dataObject = (DataObject)intent.getSerializableExtra("location");
        }

        Log.i("Data Object", dataObject.toString());

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", dataObject);

        ViewFragment frag = ViewFragment.newInstance();
        frag.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.viewContainer, frag).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view, menu);
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
