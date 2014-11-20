package com.thenewcircle.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by geoff on 11/20/14.
 */
public class YambaApp extends Application {

    private static final String TAG = "yamba." + YambaApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent refreshIntent = new Intent(this, TimelineService.class);

        PendingIntent timelineStatusPending = PendingIntent.getService(this, 900, refreshIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, timelineStatusPending);
    }
}
