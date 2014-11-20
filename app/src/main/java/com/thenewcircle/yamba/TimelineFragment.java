package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;
/**
 * Created by geoff on 11/19/14.
 */
public class TimelineFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<Cursor>{

    public interface OnMessageSelectedListener {
        public void onMessageSelected(long id);
    }

    private static final String TAG = "yamba." + TimelineFragment.class.getSimpleName();

    private SimpleCursorAdapter adapter;
    private static final String[] FROM = {MESSAGE,          TIME_CREATED,  USER};
    private static final int[]      TO = {R.id.messageText, R.id.timeText, R.id.detailsUserNameText};

    private SimpleCursorAdapter.ViewBinder rowViewBinder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if(view.getId() == R.id.timeText) {
                TextView textView = (TextView) view;
                Long time = cursor.getLong(columnIndex);
                CharSequence friendlyTime = DateUtils.getRelativeTimeSpanString(time);
                textView.setText(friendlyTime);
                return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof OnMessageSelectedListener)) {
            throw new IllegalArgumentException("Activity must implement OnMessageSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_friend_status, null, FROM, TO,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.setViewBinder(rowViewBinder);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnMessageSelectedListener listener = (OnMessageSelectedListener) getActivity();
        listener.onMessageSelected(id);
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(getActivity(), TimelineContract.CONTENT_URI, null, null, null,
                TimelineContract.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        adapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timeline, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh) {
            Intent timelineServiceIntent = new Intent(getActivity(), TimelineService.class);
            getActivity().startService(timelineServiceIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}