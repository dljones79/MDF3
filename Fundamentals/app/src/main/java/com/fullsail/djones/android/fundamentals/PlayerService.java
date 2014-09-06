// David Jones
// MDF 3 - 1409
// Week 1 - Fundamentals


package com.fullsail.djones.android.fundamentals;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PlayerService extends Service {

    public class PlayerServiceBinder extends Binder {
        public PlayerService getService() { return PlayerService.this; }
    }

    PlayerServiceBinder mBinder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new PlayerServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }



}
