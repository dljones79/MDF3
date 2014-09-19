//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AddFragment extends Fragment {

    public static final String TAG = "AddFragment.TAG";

    Button saveButton;
    EditText mEventText;
    EditText mDateText;
    EditText mNotesText;
    ToDoObject todoObj;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }


    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        todoObj = new ToDoObject();

        mEventText = (EditText) view.findViewById(R.id.eventText);
        mDateText = (EditText) view.findViewById(R.id.dateText);
        mNotesText = (EditText) view.findViewById(R.id.notesText);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = mEventText.getText().toString();
                String date = mDateText.getText().toString();
                String notes = mNotesText.getText().toString();

                todoObj.setEvent(event);
                todoObj.setDate(date);
                todoObj.setNotes(notes);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("eventName", todoObj.getEvent());
                intent.putExtra("eventDate", todoObj.getDate());
                intent.putExtra("eventNotes", todoObj.getNotes());

                finish();

            }
        });
    }

    private void finish() {
        Log.i(TAG, "Finish() Called.");
        Intent data = new Intent();
        data.putExtra("returnKey", todoObj);
        getActivity().setResult(Activity.RESULT_OK, data);
        super.getActivity().finish();
    }
}
