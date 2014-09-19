//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by David on 9/18/14.
 */
public class AppWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final int ID_CONSTANT = 0x1010101;
    private static final String TAG = "AppWidgetViewFactory.TAG";

    private ArrayList<ToDoObject> mEvents;
    private Context mContext;

    public AppWidgetViewFactory(Context context){
        mContext = context;
        mEvents = new ArrayList<ToDoObject>();
    }

    @Override
    public void onCreate(){
        // Load data from storage
        loadData();

        Log.i(TAG, mEvents.toString());

    }

    @Override
    public int getCount() { return mEvents.size(); }

    @Override
    public long getItemId(int position) { return ID_CONSTANT + position; }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        ToDoObject event = mEvents.get(position);

        RemoteViews eventView = new RemoteViews(mContext.getPackageName(), R.layout.event_item);

        eventView.setTextViewText(R.id.eventName, event.getEvent());
        eventView.setTextViewText(R.id.eventDate, event.getDate());

        Intent intent = new Intent();
        intent.putExtra(AppWidget.EXTRA_ITEM, event);
        eventView.setOnClickFillInIntent(R.id.event_item, intent);

        return eventView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    @Override
    public void onDestroy() {
        mEvents.clear();
    }

    // Method to load saved data.
    public void loadData(){
        Log.i(TAG, "Attempting to load data.");
        try {
            FileInputStream fin = mContext.openFileInput("data.txt");
            ObjectInputStream oin = new ObjectInputStream(fin);
            int count = oin.readInt();
            mEvents = new ArrayList<ToDoObject>();
            for (int i = 0; i < count; i++)
                mEvents.add((ToDoObject) oin.readObject());
            oin.close();
            Log.i(TAG, "Load success.");
        } catch (IOException e) {
            Log.e(TAG, "Load Failed!!!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Load Failed!!!");
            e.printStackTrace();
        }
    }

}
