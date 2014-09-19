//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;


import android.app.Activity;
import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class ListFragment extends Fragment {

    // Initialize Variables
    public static final String TAG = "ListFragment.TAG";
    private static final int REQUEST_CODE = 2;
    private ArrayList<ToDoObject> passedEvents;
    private EventListener mListener;
    public Boolean willAdd = false;


    public ListFragment() {
        // Required empty public constructor
    }

    // Interface
    public interface EventListener{
        public void viewEvent(int position);
        public ArrayList<ToDoObject> getEvents();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);

        if (activity instanceof EventListener){
            mListener = (EventListener) activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement EventListener interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    } // end of ListFragment newInstance()

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Get current view
        View view = getView();

        // Get button
        Button addButton = (Button) view.findViewById(R.id.addButton);

        // Get the listview and create listeners for Button and ListView
        ListView listView = (ListView) getView().findViewById(R.id.listView);
        EventAdapter eventAdapter = new EventAdapter(getActivity(), mListener.getEvents());
        listView.setAdapter(eventAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

                /*
                AddFragment frag = AddFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.listContainer, frag, AddFragment.TAG).commit();
                */
            }
        });

        // This is called when we click the Add Button from the Widget
        // This has been the only way I've been able to return a result back to the MainActivity
        // when calling the AddActivity from my Widget
        if (willAdd){
            clickAdd();
        }

        // Set a click listener for the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.viewEvent(i);
            }
        });
    }

    // Called when data is returned from the AddActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG, "onActivityResult running.");
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE){
            if (data.hasExtra("returnKey")){
                passedEvents.add((ToDoObject) data.getSerializableExtra("returnKey"));

                // Here we save data out to storage for later use
                try{
                    FileOutputStream fos = getActivity().openFileOutput("data.txt", getActivity().MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeInt(passedEvents.size());
                    for (ToDoObject e:passedEvents){
                        oos.writeObject(e);
                    }
                    oos.close();
                    Log.i(TAG, "Data Saved");
                } catch (IOException e){
                    e.printStackTrace();
                }

                // Get a new instance of AppWidgetFactory and load data
                AppWidgetViewFactory appWidgetViewFactory = new AppWidgetViewFactory(getActivity());
                appWidgetViewFactory.loadData();

                // Call custom method to update all widgets
                updateAllWidgets();

                // Update ListView on the ListFragment when data is added
                ListFragment lf = (ListFragment) getFragmentManager().findFragmentById(R.id.container);
                lf.updateListData();
            }
        } else {
            Log.i(TAG, "Error with result.");
        }
    }

    public void setEvents (ArrayList<ToDoObject> events) { passedEvents = events; }

    // Custom method to update the ListView
    private void updateListData() {
        ListView eventList = (ListView) getView().findViewById(R.id.listView);
        BaseAdapter eventAdapter = (BaseAdapter) eventList.getAdapter();
        eventAdapter.notifyDataSetChanged();
    }

    // Custom method to update all widgets running
    private void updateAllWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), AppWidget.class));
        if (appWidgetIds.length > 0) {
            new AppWidget().onUpdate(getActivity(), appWidgetManager, appWidgetIds);
        }
    }

    // Custom method to click the addbutton
    public void clickAdd(){
        View view = getView();
        Button addButton = (Button) view.findViewById(R.id.addButton);
        addButton.performClick();
    }

    // Custom method to set flag when passing intent from the Widget
    public void setFlag(){
        willAdd = true;
    }


}
