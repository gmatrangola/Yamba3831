package com.thenewcircle.yamba;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class PostActivity extends YambaActivity implements TextWatcher {

    private static final String TAG = "yamba." + PostActivity.class.getSimpleName();
    private EditText messageText;
    private TextView charCountText;
    private int maxChars;
    private MenuItem sendMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        String maxCharsString = getResources().getString(R.string.maxChars);
        maxChars = Integer.parseInt(maxCharsString);

        setContentView(R.layout.activity_post);
        messageText = (EditText) findViewById(R.id.messageText);

        charCountText = (TextView) findViewById(R.id.charCountText);

        messageText.addTextChangedListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause - clean things up");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        if(message != null) {
            messageText.setText(message);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        sendMenuItem = menu.findItem(R.id.send);
        int length = messageText.getText().length();
        sendMenuItem.setEnabled(length > 0 && length < maxChars);
        return super.onCreateOptionsMenu(menu);
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
                Intent postServiceIntent = new Intent(PostActivity.this, PostService.class);
                messageText.getText().clear();
                postServiceIntent.putExtra("message", message);
                startService(postServiceIntent);
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
