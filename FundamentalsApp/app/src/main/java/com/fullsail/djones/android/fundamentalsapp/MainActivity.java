// David Jones
// Full Sail University
// MDF 3 - 1409
// Week 1 Fundamentals

package com.fullsail.djones.android.fundamentalsapp;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity{



    // Runs when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment frag = mainFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.container, frag, mainFragment.TAG).commit();
    }

}
