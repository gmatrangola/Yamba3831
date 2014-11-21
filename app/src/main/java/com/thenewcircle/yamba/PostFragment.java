package com.thenewcircle.yamba;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by geoff on 11/21/14.
 */
public class PostFragment extends Fragment implements TextWatcher {
    private static final String TAG = "yamba." + PostFragment.class.getSimpleName();
    private int maxChars;
    private EditText messageText;
    private TextView charCountText;
    private MenuItem sendMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_post, container, false);
        String maxCharsString = getResources().getString(R.string.maxChars);
        maxChars = Integer.parseInt(maxCharsString);

        messageText = (EditText) layout.findViewById(R.id.messageText);

        charCountText = (TextView) layout.findViewById(R.id.charCountText);

        messageText.addTextChangedListener(this);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Intent intent = getActivity().getIntent();
        String message = intent.getStringExtra("message");
        if(message != null) {
            messageText.setText(message);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        sendMenuItem = menu.findItem(R.id.send);
        int length = messageText.getText().length();
        sendMenuItem.setEnabled(length > 0 && length < maxChars);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.send:
                final String message = messageText.getText().toString();
                Log.d(TAG, "Message = " + message);
                Intent postServiceIntent = new Intent(getActivity(), PostService.class);
                messageText.getText().clear();
                postServiceIntent.putExtra("message", message);
                getActivity().startService(postServiceIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int charsRemaining = maxChars - s.length();
        charCountText.setText(charsRemaining + "");
        if(charsRemaining < 0) {
            charCountText.setTextColor(getResources().getColor(R.color.error));
            if(sendMenuItem != null) sendMenuItem.setEnabled(false);
        }
        else {
            charCountText.setTextColor(getResources().getColor(R.color.valid));
            if(sendMenuItem != null) sendMenuItem.setEnabled(true);
        }
        if(sendMenuItem != null && s.length() == 0 ) sendMenuItem.setEnabled(false);

    }
}
