// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FormFragment extends Fragment implements LocationListener{

    public static final String TAG = "FormFragment.TAG";
    private static final int REQUEST_TAKE_PICTURE = 0x10101;
    private static final int REQUEST_ENABLE_GPS = 0x02101;

    // Initialize variables
    Button captureButton;
    Button saveButton;
    EditText mNameText;
    EditText mNoteText;
    DataObject dataObject;
    Uri mImageUri;
    ImageView mImageView;
    ArrayList<DataObject> locations;
    LocationManager mManager;
    Double mLatitude;
    Double mLongitude;

    public FormFragment() {
        // Required empty public constructor
    } // End FormFragment()

    public static FormFragment newInstance(){
        FormFragment fragment = new FormFragment();
        return fragment;
    } // End FormFragment newInstance()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    } // End onCreateView

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        dataObject = new DataObject();
        mManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Try to load data from file
        // If no data loaded, initialize new ArrayList
        loadData();
        if (locations == null){
            locations = new ArrayList<DataObject>();
        }

        // Get EditText and Buttons
        mNameText = (EditText) view.findViewById(R.id.nameText);
        mNoteText = (EditText) view.findViewById(R.id.noteText);
        captureButton = (Button) view.findViewById(R.id.captureButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        mImageView = (ImageView)view.findViewById(R.id.captureThumb);

        // Set listeners for buttons
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageUri = getOutputUri();
                if (mImageUri != null){
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                }
                startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
            } // End onClick
        }); // End captureButton.setOnClickListener

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = mNameText.getText().toString();
                String noteText = mNoteText.getText().toString();
                String testName = getString(R.string.alert_name);
                String testNote = getString(R.string.alert_note);

                // Test to see if all field are completed and image is captured
                if (nameText.length() != 0 && noteText.length() != 0 && mImageView.getDrawable() != null &&
                        !nameText.matches(testName) && !noteText.matches(testNote)){

                    // Set data to custom object
                    // Add object to ArrayList<DataObject> locations
                    dataObject.setName(nameText);
                    dataObject.setNote(noteText);
                    dataObject.setUri(mImageUri.toString());
                    dataObject.setLatitude(mLatitude);
                    dataObject.setLongitude(mLongitude);
                    locations.add(dataObject);

                    // Save data to file
                    try{
                        FileOutputStream fos = getActivity().openFileOutput("data.txt", getActivity().MODE_PRIVATE);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeInt(locations.size());
                        for (DataObject e:locations){
                            oos.writeObject(e);
                        }
                        oos.close();
                        Toast.makeText(getActivity().getApplicationContext(), "Saving Data.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    finish();
                } /*else {

                    // Set error messages and show alert to user
                    if (mNameText.getText().length() == 0){
                        mNameText.setText(R.string.alert_name);
                        mNameText.setTextColor(Color.RED);
                    }

                    if (mNoteText.getText().length() == 0){
                        mNoteText.setText(R.string.alert_note);
                        mNoteText.setTextColor(Color.RED);
                    }
                    // Create an alert to show
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Name, notes, and image must be saved.  Please enter required fields.")
                            .setPositiveButton("Ok", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } // End of else statement */
            } // End of onClick
        }); // End of saveButton.onClickListener

        /*
        mNameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()){
                    mNameText.setText("");
                    mNameText.setTextColor(Color.BLACK);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
                return true;
            } // End onTouch
        }); // End mNameText.setOnTouchListener

        mNoteText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()){
                    mNoteText.setText("");
                    mNoteText.setTextColor(Color.BLACK);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
                return true;
            } // End onTouch
        }); // End mNoteText.setOnTouchListener
        */
    } // End onActivityCreated

    private Uri getOutputUri(){
        String imageName = new SimpleDateFormat("MMddyyy_HHmmss")
                .format(new Date(System.currentTimeMillis()));
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File appDir = new File(imageDir, "MappingPhotos");
        appDir.mkdirs();

        File image = new File(appDir, imageName + ".jpg");
        try{
            image.createNewFile();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(image);
    } // End Uri getOutputUri

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode != getActivity().RESULT_CANCELED) {
            if(mImageUri != null) {
                mImageView.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
                addImageToGallery(mImageUri);
            } else {
                mImageView.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
            }
        }
        enableGps();
    } // End onActivityResult

    private void addImageToGallery(Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        getActivity().sendBroadcast(scanIntent);
    } // End addImageToGallery

    // Load data from file
    public void loadData(){
        try{
            FileInputStream fin = getActivity().openFileInput("data.txt");
            ObjectInputStream oin = new ObjectInputStream(fin);
            int count = oin.readInt();
            locations = new ArrayList<DataObject>();
            for (int i = 0; i < count; i++)
                locations.add((DataObject) oin.readObject());
            oin.close();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    } // end of loadData method

    // Custom method to finish FormFragment and return intent
    private void finish() {
        Intent data = new Intent();
        data.putExtra("returnKey", locations);
        getActivity().setResult(Activity.RESULT_OK, data);
        super.getActivity().finish();
    } // end of finish method

    // Enable phone GPS and get location
    private void enableGps(){
        if(mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10,this);

            Location loc = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null){
                mLatitude = (loc.getLatitude());
                mLongitude = (loc.getLongitude());
            }
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Unvailable")
                    .setMessage("Enable GPS")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                        }
                    })
                    .show();
        } // end else statement (if no GPS)
    } // end of enableGps method

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
