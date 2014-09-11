// David Jones
// Full Sail University
// MDF 3 - 1409
// Week 1 Fundamentals

package com.fullsail.djones.android.fundamentalsapp;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity{

    String title = "";
    int imageNum = 0;

    // Runs when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment frag = MainFragment.newInstance();

        if (savedInstanceState != null){
            title = savedInstanceState.getString("title");
            imageNum = savedInstanceState.getInt("img");
            frag.onRestore(title, imageNum);
        }

        getFragmentManager().beginTransaction().replace(R.id.container, frag, MainFragment.TAG).commit();
    }

}
