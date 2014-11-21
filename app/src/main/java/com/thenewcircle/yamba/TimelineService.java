package com.thenewcircle.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
        // Yamba client from the yambaclientlib
        YambaApp app = (YambaApp) getApplication();
        YambaClient yambaClient = app.getYambaClient();
        if(yambaClient == null) return;

        Cursor c = getContentResolver().query(TimelineContract.CONTENT_URI,
                TimelineContract.MAX_TIME_CREATED,
                null, null, null);
        long maxTime = c.moveToFirst()?c.getLong(0):Long.MIN_VALUE;

        try {
            List<YambaClient.Status> posts = yambaClient.getTimeline(20);

            ContentValues values = new ContentValues();
            for(YambaClient.Status status : posts) {
                if(BuildConfig.DEBUG) {
                    Log.d(TAG, "message: " + status.getMessage() + " user: " + status.getUser());
                }
                long time = status.getCreatedAt().getTime();
                if(time > maxTime) {
                    values.put(ID, status.getId());
                    values.put(MESSAGE, status.getMessage());
                    values.put(TIME_CREATED, time);
                    values.put(USER, status.getUser());
                    getContentResolver().insert(TimelineContract.CONTENT_URI, values);
                }
            }
        } catch (YambaClientException e) {
            Log.d(TAG, "Unable to update timeline.", e);
        }
    }
}
