// David Jones
// MDF 3 - 1409
// Week 1 - Fundamentals


package com.fullsail.djones.android.fundamentals;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlayerFragment pFrag = PlayerFragment.newInstance();

        getFragmentManager().beginTransaction().replace(R.id.container, pFrag, PlayerFragment.TAG).commit();
    }

}
