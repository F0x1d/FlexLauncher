package com.f0x1d.flexlauncher.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;

import com.f0x1d.flexlauncher.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
