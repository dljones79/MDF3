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


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment.TAG";
    private static final int REQUEST_CODE = 2;
    private ArrayList<ToDoObject> passedEvents;
    private EventListener mListener;
    private int mWidgetId;

    public ListFragment() {
        // Required empty public constructor
    }

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

        View view = getView();

        Button addButton = (Button) view.findViewById(R.id.addButton);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.viewEvent(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE){
            if (data.hasExtra("returnKey")){
                passedEvents.add((ToDoObject) data.getSerializableExtra("returnKey"));
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

                AppWidgetViewFactory appWidgetViewFactory = new AppWidgetViewFactory(getActivity());
                appWidgetViewFactory.loadData();

                updateAllWidgets();
                /*
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                ComponentName thisWidget = new ComponentName(getActivity(), AppWidget.class);
                int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                AppWidget appWidget = new AppWidget();
                appWidget.onUpdate(getActivity(), appWidgetManager, allWidgetIds);
                */
                /*
                for (int widgetId : allWidgetIds){

                    RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.app_widget);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);

                    appWidget.onUpdate(getActivity(), appWidgetManager, allWidgetIds);
                }
                */

                ListFragment lf = (ListFragment) getFragmentManager().findFragmentById(R.id.container);
                lf.updateListData();
            }
        }
    }

    public void setEvents (ArrayList<ToDoObject> events) { passedEvents = events; }

    private void updateListData() {
        ListView eventList = (ListView) getView().findViewById(R.id.listView);
        BaseAdapter eventAdapter = (BaseAdapter) eventList.getAdapter();
        eventAdapter.notifyDataSetChanged();
    }

    private void updateAllWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), AppWidget.class));
        if (appWidgetIds.length > 0) {
            new AppWidget().onUpdate(getActivity(), appWidgetManager, appWidgetIds);
        }
    }


}
