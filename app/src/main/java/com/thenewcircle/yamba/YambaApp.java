package com.thenewcircle.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by geoff on 11/20/14.
 */
public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "yamba." + YambaApp.class.getSimpleName();
    public static final String INTERVAL_KEY = "interval";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        updateAlarmManager(prefs);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(INTERVAL_KEY)){

            updateAlarmManager(sharedPreferences);
        }
    }

    private void updateAlarmManager(SharedPreferences prefs) {
        Log.d(TAG, "updateAlarmManager");
        Intent refreshIntent = new Intent(this, TimelineService.class);
        PendingIntent timelineStatusPending = PendingIntent.getService(this, 900, refreshIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(timelineStatusPending);

        String updateInterval = prefs.getString(INTERVAL_KEY, "0");
        long interval = Long.parseLong(updateInterval);
        if(interval > 0) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000,
                    interval, timelineStatusPending);
        }
    }


}
