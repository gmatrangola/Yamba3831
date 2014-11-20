package com.thenewcircle.yamba;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by geoff on 11/20/14.
 */
public class YambaWidget extends AppWidgetProvider {

    private static final String TAG = "yamba." + YambaWidget.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        this.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new
                ComponentName(context, YambaWidget.class)));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");

        Cursor cursor = context.getContentResolver().query( TimelineContract.CONTENT_URI, null,
                null, null, TimelineContract.DEFAULT_SORT_ORDER);

        if(!cursor.moveToFirst()) return;

        String user = cursor.getString(cursor.getColumnIndex(TimelineContract.Columns.USER));
        String message = cursor.getString(cursor.getColumnIndex(TimelineContract.Columns.MESSAGE));
        long createdAt = cursor.getLong(cursor.getColumnIndex(TimelineContract.Columns.TIME_CREATED));

        String friendlyTime = DateUtils.formatElapsedTime(createdAt);

        PendingIntent operation = PendingIntent.getActivity(context, -1,
                new Intent(context, TimelineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        for(int appWidgetId : appWidgetIds) {
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget);

            view.setTextViewText(R.id.detailsUserNameText, user);
            view.setTextViewText(R.id.detailsMessageText, message);
            view.setTextViewText(R.id.detailsTimeText, friendlyTime);

            view.setOnClickPendingIntent(R.id.detailsUserNameText, operation);
            view.setOnClickPendingIntent(R.id.detailsMessageText, operation);

            appWidgetManager.updateAppWidget(appWidgetId, view);
        }
    }
}
