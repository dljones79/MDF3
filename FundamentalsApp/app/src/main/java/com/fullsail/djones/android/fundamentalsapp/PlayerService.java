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

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    public static final int identifier = 1409;
    MediaPlayer mediaPlayer;
    boolean mPlayerPrepared;
    boolean mActivityResumed;
    int mTrackPosition;
    String[] trackArray;
    String[] songTitles;
    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;

    @Override
    public void onCreate(){
        super.onCreate();
        //Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();

        trackArray = new String[]{"/raw/neo_western", "/raw/summon_the_rawk", "/raw/take_the_lead", "/raw/zap_beat"};
        songTitles = new String[]{"Neo Western", "Summon the Rawk", "Take the Lead", "Zap Beat"};

        mPlayerPrepared = mActivityResumed = false;
        mTrackPosition = 0;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null){
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

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

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
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
        } else {
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

        String songPlaying = songTitles[mTrackPosition];
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        /*
        Notification.Builder nBuilder = new Notification.Builder(this);

        nBuilder.setContentIntent(pIntent);
        nBuilder.setSmallIcon(R.drawable.ic_launcher);
        nBuilder.setTicker(songPlaying);
        nBuilder.setOngoing(true);
        nBuilder.setContentText(songPlaying);
        nBuilder.setContentTitle("Listening To:");

        Notification notification = nBuilder.build();

        startForeground(identifier, notification);
        */
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setContentTitle("Listening To: ");
        builder.setContentText(songPlaying);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("Some awesome music on a less than awesome music player!");
        bigTextStyle.setBigContentTitle("You are Listening To: ");
        bigTextStyle.setSummaryText("A song called: " + songPlaying);
        builder.setStyle(bigTextStyle);
        manager.notify(EXPANDED_NOTIFICATION, builder.build());

    }

    // Internal Binder Class
    public class ServiceBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    // Player Controls
    public void onPlay() {
        mTrackPosition = 0;
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

    public void onPause() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        } else {
            this.onResume();
        }

    }

    public void onResume() {
        mActivityResumed = true;
        if (mediaPlayer != null && !mPlayerPrepared){
            mediaPlayer.prepareAsync();
        } else if (mediaPlayer != null && mPlayerPrepared) {
            mediaPlayer.start();
        }
    }

    public void onStop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mPlayerPrepared = false;
        stopForeground(true);
    }

    public void onBack(){
        if (mTrackPosition == 0){
            mTrackPosition = trackArray.length -1;
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerService.this);
            mediaPlayer.setOnCompletionListener(PlayerService.this);

            try{
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            } catch (IOException e){
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

            try{
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            } catch (IOException e){
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        }
    }

    public void onForward(){
        if (mTrackPosition < trackArray.length - 1){
            mTrackPosition++;
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerService.this);
            mediaPlayer.setOnCompletionListener(PlayerService.this);

            try{
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            } catch (IOException e){
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

            try{
                mediaPlayer.setDataSource(PlayerService.this, Uri.parse("android.resource://" + getPackageName() + trackArray[mTrackPosition]));
            } catch (IOException e){
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        }
    }

}
