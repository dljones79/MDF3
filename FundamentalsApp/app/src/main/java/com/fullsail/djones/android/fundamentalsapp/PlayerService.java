// David Jones
// Full Sail University
// MDF 3 - 1409
// Week 1 Fundamentals


package com.fullsail.djones.android.fundamentalsapp;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.Random;

// Custom Service Class for Media Player
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    // Initialize variables
    MediaPlayer mediaPlayer;                                    // MediaPlayer
    boolean mPlayerPrepared;                                    // Boolean to check if player is prepared
    boolean mActivityResumed;                                   // Boolean to check if the player is resuming play
    int mTrackPosition;                                         // Integer to check what track is playing
    String[] trackArray;                                        // Array to hold file names of audio from raw file
    String[] songTitles;                                        // Array to hold names of songs
    String[] imageTitles;
    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;

    //Shuffle Variables
    private boolean shuffle = false;
    int min = 0;
    int max = 3;
    private Random random;

    @Override
    public void onCreate(){
        super.onCreate();
        //Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();

        // Assign data to string arrays
        trackArray = new String[]{"/raw/neo_western", "/raw/summon_the_rawk", "/raw/take_the_lead", "/raw/zap_beat"};
        songTitles = new String[]{"Neo Western", "Summon the Rawk", "Take the Lead", "Zap Beat"};
        imageTitles = new String[]{"neo", "summon", "lead", "zap"};

        // Get random number for shuffle implementation (constrained to size of song array
        random = new Random();

        // Set prepared and resumed states to false and track position to zero
        mPlayerPrepared = mActivityResumed = false;
        mTrackPosition = 0;

        // Check player status, set stream type, set OnPreparedListener, and set the data source
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);

            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/neo_western"));
            } catch (IOException e) {
                e.printStackTrace();

                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

    }

    // Called on start
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null){
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    // Called on destroy
    @Override
    public void onDestroy(){
        super.onDestroy();

        // If the media player is playing
        if (mediaPlayer != null){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    // When a song is complete
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        // If we're playing any song before last one in array
        if (mTrackPosition < trackArray.length - 1){
            mTrackPosition++;
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerService.this);
            mediaPlayer.setOnCompletionListener(PlayerService.this);

            try{
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + "/" + trackArray[mTrackPosition]));
            } catch (IOException e){
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync();
        }
        // If we're at the last song in the array
        else {
            mTrackPosition = 0;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerService.this);
            mediaPlayer.setOnCompletionListener(PlayerService.this);

            try{
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + "/" + trackArray[mTrackPosition]));
            } catch (IOException e){
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        }
    }

    // TargetApi used in order to utilize builder.build();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mPlayerPrepared = true;

        if (mediaPlayer != null && mActivityResumed) {
            mediaPlayer.seekTo(mTrackPosition);
            mediaPlayer.start();
        } else {
            mediaPlayer.start();
        }

        // Create a string to hold title of the song being played
        String songPlaying = songTitles[mTrackPosition];

        // New intent
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Create a notification item for the media player
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pIntent);
        builder.setSmallIcon(R.drawable.mp);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mp));
        builder.setContentTitle("Listening To: ");
        builder.setContentText(songPlaying);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("Some awesome music on a less than awesome music player!");
        bigTextStyle.setBigContentTitle("You are Listening To: ");
        bigTextStyle.setSummaryText("A song called: " + songPlaying);
        builder.setStyle(bigTextStyle);
        //manager.notify(EXPANDED_NOTIFICATION, builder.build());
        startForeground(EXPANDED_NOTIFICATION, builder.build());

    }

    // Internal Binder Class
    public class ServiceBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    // Player Controls
    // When play button is pressed
    // Set track position to zero (First Song in Array)
    // Start up a new media player
    // Set Listeners and Data Source
    // Prepare to play
    public void onPlay() {
        if (shuffle){
            mTrackPosition = random.nextInt(max - min + 1) + min;
        } else {
            mTrackPosition = 0;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(PlayerService.this);
        mediaPlayer.setOnCompletionListener(PlayerService.this);

        try{
            mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            mediaPlayer.prepareAsync();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // When pause button is pressed
    // Pause the song if playing
    // Resume the song if paused
    public void onPause() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        } else {
            this.onResume();
        }

    }

    // When a song is resumed from pause
    public void onResume() {
        mActivityResumed = true;
        if (mediaPlayer != null && !mPlayerPrepared){
            mediaPlayer.prepareAsync();
        } else if (mediaPlayer != null && mPlayerPrepared) {
            mediaPlayer.start();
        }
    }

    // When stop button is pressed
    // Stop the song
    // Release the media player
    public void onStop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mPlayerPrepared = false;
        stopForeground(true);
    }

    // When back button is pressed
    // Check to see what position we are at
    // If at first position, we skip back to the end of the array
    // If not at first position, we just skip back one song
    public void onBack(){
        if (shuffle){
            mTrackPosition = random.nextInt(max - min + 1) + min;
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerService.this);
            mediaPlayer.setOnCompletionListener(PlayerService.this);

            try {
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        } else {

            if (mTrackPosition == 0) {
                mTrackPosition = trackArray.length - 1;
                mediaPlayer.reset();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(PlayerService.this);
                mediaPlayer.setOnCompletionListener(PlayerService.this);

                try {
                    mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
            } else {
                mTrackPosition--;
                mediaPlayer.reset();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(PlayerService.this);
                mediaPlayer.setOnCompletionListener(PlayerService.this);

                try {
                    mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
            }
        }
    }

    // When forward button is pressed
    // Basically same functionality as back button except in reverse
    public void onForward(){
        if (shuffle){
            mTrackPosition = random.nextInt(max - min + 1) + min;
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerService.this);
            mediaPlayer.setOnCompletionListener(PlayerService.this);

            try {
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        } else {
            if (mTrackPosition < trackArray.length - 1) {
                mTrackPosition++;
                mediaPlayer.reset();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(PlayerService.this);
                mediaPlayer.setOnCompletionListener(PlayerService.this);

                try {
                    mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
            } else {
                mTrackPosition = 0;
                mediaPlayer.reset();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(PlayerService.this);
                mediaPlayer.setOnCompletionListener(PlayerService.this);

                try {
                    mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
            }
        }
    }

    public void onShuffle(Boolean bool) {

        if (bool){
            shuffle = true;
        } else {
            shuffle = false;
        }
    }
}
