package com.fullsail.djones.android.mappingphotos;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormFragment extends Fragment {

    public static final String TAG = "FormFragment.TAG";
    private static final int REQUEST_TAKE_PICTURE = 0x10101;

    // Initialize variables
    Button captureButton;
    Button saveButton;
    EditText mNameText;
    EditText mNoteText;
    Bitmap mBitmap;
    DataObject dataObject;
    Uri mImageUri;
    ImageView mImageView;

    public FormFragment() {
        // Required empty public constructor
    }

    public static FormFragment newInstance(){
        FormFragment fragment = new FormFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        dataObject = new DataObject();

        mNameText = (EditText) view.findViewById(R.id.nameText);
        mNoteText = (EditText) view.findViewById(R.id.noteText);
        captureButton = (Button) view.findViewById(R.id.captureButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        mImageView = (ImageView)view.findViewById(R.id.captureThumb);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageUri = getOutputUri();
                if (mImageUri != null){
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                }
                startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = mNameText.getText().toString();
                String noteText = mNoteText.getText().toString();
                String testName = getString(R.string.alert_name);
                String testNote = getString(R.string.alert_note);

                if (nameText.length() != 0 && noteText.length() != 0 && mImageView.getDrawable() != null &&
                        !nameText.matches(testName) && !noteText.matches(testNote)){
                    Toast.makeText(getActivity().getApplicationContext(), "Will Save.", Toast.LENGTH_SHORT).show();

                } else {
                    if (mNameText.getText().length() == 0){
                        mNameText.setText(R.string.alert_name);
                        mNameText.setTextColor(Color.RED);
                    }

                    if (mNoteText.getText().length() == 0){
                        mNoteText.setText(R.string.alert_note);
                        mNoteText.setTextColor(Color.RED);
                    }
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Name, notes, and image must be saved.  Please enter required fields.")
                            .setPositiveButton("Ok", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            }
        });

        mNameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()){
                    mNameText.setText("");
                    mNameText.setTextColor(Color.BLACK);
                }
                return true;
            }
        });

        mNoteText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()){
                    mNoteText.setText("");
                    mNoteText.setTextColor(Color.BLACK);
                }
                return true;
            }
        });

    }

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
    }

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
    }

    private void addImageToGallery(Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        getActivity().sendBroadcast(scanIntent);
    }
}
