package com.fullsail.djones.android.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 9/17/14.
 */
public class EventAdapter extends BaseAdapter {

    private static final long ID_CONSTANT = 0x01000000;

    Context mContext;
    ArrayList<ToDoObject> mEvents;

    public EventAdapter(Context context, ArrayList<ToDoObject> events){
        mContext = context;
        mEvents = events;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false);
        }

        ToDoObject event = (ToDoObject) getItem(position);
        TextView eventNameView = (TextView) v.findViewById(R.id.eventName);
        eventNameView.setText(event.getEvent());

        return v;
    }
}
