// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;


import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ViewFragment extends Fragment {

    DataObject passedObject;
    TextView locName;
    TextView locNote;
    ImageView locImage;
    String imgUri;
    Uri parsedUriString;

    public ViewFragment() {
        // Required empty public constructor
    }

    public static ViewFragment newInstance(){
        ViewFragment fragment = new ViewFragment();
        return fragment;
    } // End ViewFragment newInstance()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Attempt to load passed bundle
        passedObject = new DataObject();
        passedObject = (DataObject) getArguments().getSerializable("data");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check to see if we have passed data
        if (passedObject != null){
            Log.i("Passed Object", "Not Null");
        } else {
            Log.i("Passed Object", "Null");
        }

        locName = (TextView)getActivity().findViewById(R.id.nameText);
        locNote = (TextView)getActivity().findViewById(R.id.noteText);
        locImage = (ImageView)getActivity().findViewById(R.id.locationImage);

        locName.setText(passedObject.getName().toString());
        locNote.setText(passedObject.getNote().toString());

        imgUri = passedObject.getUri();
        parsedUriString = Uri.parse(imgUri);

        locImage.setImageBitmap(BitmapFactory.decodeFile(parsedUriString.getPath()));

    }


}
