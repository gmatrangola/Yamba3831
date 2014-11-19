package com.thenewcircle.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;
import java.util.List;

/**
 * Created by geoff on 11/18/14.
 */
public class TimelineService extends IntentService {

    private static final String TAG = "yamba." + TimelineService.class.getSimpleName();

    public TimelineService() {
        super(TimelineService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = prefs.getString("userName", null);
        String password = prefs.getString("password", null);

        YambaClient yambaClient = new YambaClient(userName, password);
        try {
            List<YambaClient.Status> posts = yambaClient.getTimeline(20);

            ContentValues values = new ContentValues();
            for(YambaClient.Status status : posts) {
                Log.d(TAG, "message: " + status.getMessage() + " user: " + status.getUser());
                values.put(ID, status.getId());
                values.put(MESSAGE, status.getMessage());
                values.put(TIME_CREATED, status.getCreatedAt().getTime());
                values.put(USER, status.getUser());
                getContentResolver().insert(TimelineContract.CONTENT_URI, values);
            }
        } catch (YambaClientException e) {
            Log.d(TAG, "Unable to update timeline.", e);
        }
    }
}
