package com.thenewcircle.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by geoff on 11/20/14.
 */
public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "yamba." + YambaApp.class.getSimpleName();
    public static final String INTERVAL_KEY = "interval";
    private YambaClient yambaClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        updateAlarmManager(prefs);
        connectToYamba(prefs);
    }

    private void connectToYamba(SharedPreferences prefs) {
        // Yamba client from the yambaclientlib

        String userName = prefs.getString("userName", null);
        String password = prefs.getString("password", null);
        if(userName == null || password == null || userName.length() == 0 || password.length() == 0){
            Notification.Builder userNotification = new Notification.Builder(this);
            userNotification.setSmallIcon(R.drawable.ic_launcher);
            userNotification.setContentTitle("Unable to connect to Yamba");
            userNotification.setContentText("Click here to set Username and password");
            Intent prefsIntent = new Intent(this, SettingsActivity.class);
            PendingIntent pendingPrefs = PendingIntent.getActivity(this, 500, prefsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // userNotification.addAction(R.drawable.ic_launcher, "Settings", pendingPrefs);
            userNotification.setContentIntent(pendingPrefs);
            userNotification.setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(300, userNotification.getNotification());
            yambaClient = null;
            return;
        }

        yambaClient = new YambaClient(userName, password);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(INTERVAL_KEY)){
            updateAlarmManager(sharedPreferences);
        }
        if(key.equals("userName") || key.equals("password")) connectToYamba(sharedPreferences);
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


    public YambaClient getYambaClient() {
        return yambaClient;
    }
}
