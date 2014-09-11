package com.fullsail.djones.android.fundamentalsapp;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class mainFragment extends Fragment implements View.OnClickListener, ServiceConnection {

    // Initialize variables
    PlayerService playerService;                    // Instance of custom Service class
    PlayerService.ServiceBinder serviceBinder;      // Instance of Binder within PlayerService
    Button playButton;                              // Button variables
    Button pauseButton;
    Button stopButton;
    Button forwardButton;
    Button backButton;
    CheckBox shuffleBox;
    TextView songTitle;                             // TextView variable
    ImageView songImage;
    public static final String TAG = "mainFragment.TAG";

    public mainFragment() {
        // Required empty public constructor
    }

    public static mainFragment newInstance(){
        mainFragment fragment = new mainFragment();
        return fragment;
    } // end of mainFragment newInstance()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        // Assign buttons to button variables and set OnClickListeners to them.
        // Also set whether or not the buttons are enabled.
        playButton = (Button) view.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        pauseButton = (Button) view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        pauseButton.setEnabled(false);
        stopButton = (Button) view.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        stopButton.setEnabled(false);
        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        backButton.setEnabled(false);
        forwardButton = (Button) view.findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(this);
        forwardButton.setEnabled(false);
        shuffleBox = (CheckBox) view.findViewById(R.id.shuffleBox);
        shuffleBox.setOnClickListener(this);

        // Set the text view variable and clear out the text.
        songTitle = (TextView) view.findViewById(R.id.songText);
        songTitle.setText("");

        // Set the image view
        songImage = (ImageView) view.findViewById(R.id.imageView);
    }


    // Called when a button is clicked
    @Override
    public void onClick(View v) {

        // Create a new Intent and start the service
        Intent intent = new Intent(getActivity(), PlayerService.class);
        getActivity().startService(intent);

        // Runs methods according to what button was pressed.
        // Also sets whether or not buttons are enabled.
        if (v.getId() == R.id.playButton) {
            if (shuffleBox.isChecked()){
                playerService.onShuffle(true);
            } else {
                playerService.onShuffle(false);
            }

            playerService.onPlay();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
            if (playerService.mTrackPosition == 0){
                songImage.setImageResource(R.drawable.neo);
            } else if (playerService.mTrackPosition == 1){
                songImage.setImageResource(R.drawable.summon);
            } else if (playerService.mTrackPosition == 2){
                songImage.setImageResource(R.drawable.lead);
            } else if (playerService.mTrackPosition == 3){
                songImage.setImageResource(R.drawable.zap);
            }
        } else if (v.getId() == R.id.stopButton) {
            playerService.onStop();
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
            pauseButton.setEnabled(false);
            forwardButton.setEnabled(false);
            backButton.setEnabled(false);
            songTitle.setText("Music Stopped.");
        } else if (v.getId() == R.id.pauseButton) {
            playerService.onPause();
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
            if (playerService.mTrackPosition == 0){
                songImage.setImageResource(R.drawable.neo);
            } else if (playerService.mTrackPosition == 1){
                songImage.setImageResource(R.drawable.summon);
            } else if (playerService.mTrackPosition == 2){
                songImage.setImageResource(R.drawable.lead);
            } else if (playerService.mTrackPosition == 3){
                songImage.setImageResource(R.drawable.zap);
            }
        } else if (v.getId() == R.id.backButton) {

            if (shuffleBox.isChecked()){
                playerService.onShuffle(true);
            } else {
                playerService.onShuffle(false);
            }

            playerService.onBack();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
            if (playerService.mTrackPosition == 0){
                songImage.setImageResource(R.drawable.neo);
            } else if (playerService.mTrackPosition == 1){
                songImage.setImageResource(R.drawable.summon);
            } else if (playerService.mTrackPosition == 2){
                songImage.setImageResource(R.drawable.lead);
            } else if (playerService.mTrackPosition == 3){
                songImage.setImageResource(R.drawable.zap);
            }
        } else if (v.getId() == R.id.forwardButton) {

            if (shuffleBox.isChecked()){
                playerService.onShuffle(true);
            } else {
                playerService.onShuffle(false);
            }

            playerService.onForward();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
            if (playerService.mTrackPosition == 0){
                songImage.setImageResource(R.drawable.neo);
            } else if (playerService.mTrackPosition == 1){
                songImage.setImageResource(R.drawable.summon);
            } else if (playerService.mTrackPosition == 2){
                songImage.setImageResource(R.drawable.lead);
            } else if (playerService.mTrackPosition == 3){
                songImage.setImageResource(R.drawable.zap);
            }
        }
    }

    // Start the PlayerService onStart
    @Override
    public void onStart(){
        super.onStart();
        Intent intent = new Intent(getActivity(), PlayerService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
        Log.i(TAG, "Service Started.");
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        serviceBinder = (PlayerService.ServiceBinder)iBinder;
        playerService = serviceBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        getActivity().unbindService(mainFragment.this);
    }
}
