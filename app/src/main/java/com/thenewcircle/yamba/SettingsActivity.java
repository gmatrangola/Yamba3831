package com.thenewcircle.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.R;
/**
 * Created by geoff on 11/18/14.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.content, new YambaPrefsFragment()).commit();
    }
}
