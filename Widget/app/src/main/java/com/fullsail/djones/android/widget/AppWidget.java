//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    public static final String ACTION_VIEW_DETAILS = "com.fullsail.djones.android.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "com.fullsail.djones.android.AppWidget.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        */
        for (int i = 0; i < appWidgetIds.length; i++) {

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, AppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            widgetView.setRemoteAdapter(R.id.event_list, intent);
            widgetView.setEmptyView(R.id.event_list, R.id.empty);

            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.event_list, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, widgetView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals(ACTION_VIEW_DETAILS)){
            ToDoObject event = (ToDoObject)intent.getSerializableExtra(EXTRA_ITEM);
            if (event != null){
                Intent details = new Intent(context, DetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(DetailsActivity.EVENTEXTRA, event);
                context.startActivity(details);
            }
        }
        super.onReceive(context, intent);
    }

}


