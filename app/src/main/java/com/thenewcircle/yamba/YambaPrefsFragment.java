package com.thenewcircle.yamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by geoff on 11/18/14.
 */
public class YambaPrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
