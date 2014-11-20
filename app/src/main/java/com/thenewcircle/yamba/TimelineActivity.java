package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

/**
 * Created by geoff on 11/19/14.
 */
public class TimelineActivity extends YambaActivity implements
    TimelineFragment.OnMessageSelectedListener {

    private FrameLayout listContainer;
    private TimelineDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        FrameLayout detailsContainer = (FrameLayout) findViewById(R.id.details_container);
        TimelineFragment timelineFragment = new TimelineFragment();

        listContainer = (FrameLayout) findViewById(R.id.fragment_container);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        if(detailsContainer != null) {
            detailsFragment = new TimelineDetailsFragment();
            tx.replace(R.id.details_container, detailsFragment, "details");
        }
        tx.replace(R.id.fragment_container, timelineFragment, "list");

        tx.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent timelineServiceIntent = new Intent(this, TimelineService.class);
        startService(timelineServiceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.startPost) {
            Intent postIntent = new Intent(this, PostActivity.class);
            startActivity(postIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called by the TimelineFragment to indicate a user choice
     * @param id
     */
    @Override
    public void onMessageSelected(long id) {
        if(detailsFragment != null) {
            detailsFragment.updateView(id);
        }
        else {
            // swap out Fragment
            FragmentTransaction tx = getFragmentManager().beginTransaction();
            TimelineDetailsFragment timelineDetails = new TimelineDetailsFragment();
            tx.replace(R.id.fragment_container, timelineDetails);
            timelineDetails.setRowId(id);
            tx.addToBackStack("Details");
            tx.commit();
        }
    }
}
