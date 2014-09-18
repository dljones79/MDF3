package com.fullsail.djones.android.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class DetailsActivity extends Activity implements DetailsFragment.DetailListener{

    private static final String TAG = "DetailsActivity.TAG";

    private ToDoObject event;

    public static final String EVENTEXTRA = "com.fullsail.djones.android.widget.Event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailsFragment())
                    .commit();
        }

        Intent detailIntent = getIntent();
        if (detailIntent != null){
            event = (ToDoObject) detailIntent.getSerializableExtra(EVENTEXTRA);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
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

    @Override
    public ToDoObject getEvent() {
        return event;
    }
}
