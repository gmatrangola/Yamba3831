package com.thenewcircle.yamba;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;

/**
 * Created by geoff on 11/19/14.
 */
public class TimelineDetailsFragment extends Fragment {

    private TextView detailsUserNameText;
    private TextView detailsMessageText;
    private TextView detailsTimeText;

    private Long rowId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_details, container, false);
        detailsUserNameText = (TextView) layout.findViewById(R.id.detailsUserNameText);
        detailsMessageText = (TextView) layout.findViewById(R.id.detailsMessageText);
        detailsTimeText = (TextView) layout.findViewById(R.id.detailsTimeText);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(rowId != null) {
            updateView(rowId);
        }
    }

    public void updateView(Long id) {
        if(id == null || id == -1) {
            detailsMessageText.setText("");
            detailsUserNameText.setText("");
            detailsTimeText.setText("");
        }
        else {
            Uri uri = ContentUris.withAppendedId(TimelineContract.CONTENT_URI,
                    id);
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if(!cursor.moveToFirst()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error getting details");
                builder.setMessage("No data for ID " + id);
                builder.create().show();
                return;
            }

            detailsMessageText.setText(cursor.getString(cursor.getColumnIndex(MESSAGE)));
            detailsUserNameText.setText(cursor.getString(cursor.getColumnIndex(USER)));
            long time = cursor.getLong(cursor.getColumnIndex(TIME_CREATED));
            detailsTimeText.setText(DateUtils.getRelativeTimeSpanString(time));
        }
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}
