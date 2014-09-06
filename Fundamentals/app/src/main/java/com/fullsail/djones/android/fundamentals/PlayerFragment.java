// David Jones
// MDF 3 - 1409
// Week 1 - Fundamentals

package com.fullsail.djones.android.fundamentals;



import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, ServiceConnection{

    PlayerService mService;
    boolean mBound;

    public static final String TAG = "PlayerFragment.TAG";

    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    } // end of PlayerFragment newInstance()
    

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false);
    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        PlayerService.PlayerServiceBinder binder = (PlayerService.PlayerServiceBinder)iBinder;
        mService = binder.getService();
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
        mBound = false;
    }
}
