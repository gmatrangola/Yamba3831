package com.thenewcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * Created by geoff on 11/18/14.
 */
public class PostService extends IntentService {

    private static final String TAG = "yamba." + PostService.class.getSimpleName();
    public static final int MESSAGE_NOTIFICATION_ID = 100;

    public PostService() {
        super(PostService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        // Message text from the intent fired by the Activity
        String message = intent.getStringExtra("message");

        // Yamba client from the yambaclientlib
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = prefs.getString("userName", null);
        String password = prefs.getString("password", null);
        // NEVER Do this in real life!!!
        Log.d(TAG, "userName = " + userName + " password = " + password);
        final YambaClient yambaClient = new YambaClient(userName, password);

        // Build the common notification stuff
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentText("Message: " + message);
        builder.setSmallIcon(R.drawable.ic_launcher);
        // Set up pending intent for when user clicks on the Intent Text
        Intent postIntent = new Intent(this, PostActivity.class);
        postIntent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 200, postIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        try {
            // Post message
            yambaClient.postStatus(message);
            builder.setContentTitle("Posted");
            // Send notification to the Manager
            notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.getNotification());
        } catch (YambaClientException e) {
            // Error notification
            builder.setContentTitle("Error posting");
            notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.getNotification());
            Log.e(TAG, "Unable to post " + message, e);
        }
    }
}
