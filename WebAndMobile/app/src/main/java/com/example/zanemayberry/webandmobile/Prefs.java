package com.example.zanemayberry.webandmobile;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by zanemayberry on 11/4/14.
 */
public class Prefs extends PreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
