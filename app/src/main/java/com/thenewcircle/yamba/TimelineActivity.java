package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Created by geoff on 11/19/14.
 */
public class TimelineActivity extends Activity {

    private FrameLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        TimelineFragment timelineFragment = new TimelineFragment();

        listContainer = (FrameLayout) findViewById(R.id.fragment_container);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_container, timelineFragment, "details");
        tx.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent timelineServiceIntent = new Intent(this, TimelineService.class);
        startService(timelineServiceIntent);
    }
}
