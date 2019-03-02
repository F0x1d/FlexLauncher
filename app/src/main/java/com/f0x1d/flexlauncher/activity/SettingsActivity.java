package com.f0x1d.flexlauncher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.f0x1d.flexlauncher.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);

        toolbar.setLayoutParams(layoutParams);

        setActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        addPreferencesFromResource(R.xml.settings);

        Preference titleSize = findPreference("titleSize");
        titleSize.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                final EditText editText = new EditText(SettingsActivity.this);
                editText.setText(String.valueOf(
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("titleSize", 15)));
                editText.setHint("15");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                builder2.setView(editText);
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putInt("titleSize", Integer.parseInt(editText.getText().toString())).apply();
                    }
                });
                builder2.show();
                return false;
            }
        });

        Preference iconSize = findPreference("iconSize");
        iconSize.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] items = new String[]{"Small", "Medium", "Big", "My size"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                        .putInt("iconSize", 64).apply();
                                break;
                            case 1:
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                        .putInt("iconSize", 128).apply();
                                break;
                            case 2:
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                        .putInt("iconSize", 256).apply();
                                break;
                            case 3:
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                                final EditText editText = new EditText(SettingsActivity.this);
                                editText.setText(String.valueOf(
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("iconSize", 128)));
                                editText.setHint("128");
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                                builder2.setView(editText);
                                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                                .putInt("iconSize", Integer.parseInt(editText.getText().toString())).apply();
                                    }
                                });
                                builder2.show();
                                break;
                        }
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
