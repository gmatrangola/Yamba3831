package com.thenewcircle.yamba;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;
/**
 * Created by geoff on 11/19/14.
 */
public class TimelineFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "yamba." + TimelineFragment.class.getSimpleName();

    private SimpleCursorAdapter adapter;
    private static final String[] FROM = {MESSAGE,          TIME_CREATED,  USER};
    private static final int[]      TO = {R.id.messageText, R.id.timeText, R.id.userNameText};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_friend_status, null, FROM, TO,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
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
}