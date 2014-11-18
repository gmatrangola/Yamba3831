package com.thenewcircle.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * Created by geoff on 11/18/14.
 */
public class PostService extends IntentService {

    private static final String TAG = "yamba." + PostService.class.getSimpleName();

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
        String message = intent.getStringExtra("message");
        final YambaClient yambaClient = new YambaClient("student", "password");
        try {
            yambaClient.postStatus(message);
        } catch (YambaClientException e) {
            Log.e(TAG, "Unable to post " + message, e);
        }
    }
}
