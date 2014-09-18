package com.fullsail.djones.android.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by David on 9/18/14.
 */
public class AppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        return new AppWidgetViewFactory(getApplicationContext());
    }
}
