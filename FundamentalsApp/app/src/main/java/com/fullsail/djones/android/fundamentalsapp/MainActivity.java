package com.fullsail.djones.android.fundamentalsapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener, ServiceConnection{

    PlayerService playerService;
    PlayerService.ServiceBinder serviceBinder;
    Button playButton;
    Button pauseButton;
    Button stopButton;
    Button forwardButton;
    Button backButton;
    TextView songTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        pauseButton.setEnabled(false);
        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        stopButton.setEnabled(false);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        backButton.setEnabled(false);
        forwardButton = (Button) findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(this);
        forwardButton.setEnabled(false);

        songTitle = (TextView) findViewById(R.id.songText);
        songTitle.setText("");

    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, PlayerService.class);
        //intent.putExtra(EXTRA_RECEIVER, new DataReceiver());
        startService(intent);

        if (v.getId() == R.id.playButton) {
            playerService.onPlay();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
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
        } else if (v.getId() == R.id.backButton) {
            playerService.onBack();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
        } else if (v.getId() == R.id.forwardButton) {
            playerService.onForward();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            forwardButton.setEnabled(true);
            backButton.setEnabled(true);
            songTitle.setText("Now Playing: " + playerService.songTitles[playerService.mTrackPosition]);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        serviceBinder = (PlayerService.ServiceBinder)iBinder;
        playerService = serviceBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        unbindService(MainActivity.this);
    }
}
