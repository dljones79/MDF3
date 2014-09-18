//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class MainActivity extends Activity implements ListFragment.EventListener{

    public ToDoObject event = new ToDoObject();
    public ArrayList<ToDoObject> events = new ArrayList<ToDoObject>();
    private static final String TAG = "MainActivity.TAG";
    static final String STATE_ARRAY = "events";

    //Widget
    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null){
            event.setEvent(intent.getStringExtra("eventName"));
            event.setDate(intent.getStringExtra("eventDate"));
            event.setNotes(intent.getStringExtra("eventNotes"));
        }

        loadData();

        if (savedInstanceState == null){
            ListFragment frag = ListFragment.newInstance();
            try {
                frag.setEvents(events);
            } catch (Exception e){
                e.printStackTrace();
            }
            getFragmentManager().beginTransaction().replace(R.id.container, frag, ListFragment.TAG).commit();
        } else {
            Log.i(TAG, "Saved instance state != null");
        }

    }

    public void loadData(){
        try {
            FileInputStream fin = openFileInput("data.txt");
            ObjectInputStream oin = new ObjectInputStream(fin);
            int count = oin.readInt();
            events = new ArrayList<ToDoObject>();
            for (int i = 0; i < count; i++)
                events.add((ToDoObject) oin.readObject());
            oin.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewEvent(int position) {
        Intent detailIntent = new Intent (this, DetailsActivity.class);
        detailIntent.putExtra(DetailsActivity.EVENTEXTRA, events.get(position));
        startActivity(detailIntent);
    }

    @Override
    public ArrayList<ToDoObject> getEvents() {
        return events;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable(STATE_ARRAY, events);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        events = (ArrayList<ToDoObject>) savedInstanceState.getSerializable(STATE_ARRAY);
    }

}
