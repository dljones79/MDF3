//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";

    private DetailListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public interface DetailListener {
        public ToDoObject getEvent();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);

        if (activity instanceof DetailListener){
            mListener = (DetailListener) activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement DetailListener interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Set text according to what item was clicked on the list view
        // Data was passed over
        final TextView eventName = (TextView) getView().findViewById(R.id.eventText);
        eventName.setText(mListener.getEvent().getEvent());
        final TextView eventDate = (TextView) getView().findViewById(R.id.dateText);
        eventDate.setText(mListener.getEvent().getDate());
        final TextView eventNotes = (TextView) getView().findViewById(R.id.notesText);
        eventNotes.setText(mListener.getEvent().getNotes());
    }

}
