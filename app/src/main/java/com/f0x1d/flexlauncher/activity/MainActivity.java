package com.f0x1d.flexlauncher.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.f0x1d.flexlauncher.R;
import com.f0x1d.flexlauncher.fragment.MainFragment;
import com.f0x1d.flexlauncher.view.MySurfaceView;

import java.io.IOException;

import static com.f0x1d.flexlauncher.view.MySurfaceView.hasActiveHolder;

public class MainActivity extends AppCompatActivity {

    public static MySurfaceView MySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySurfaceView = findViewById(R.id.surfaceView);

        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("flex", true)){
            getWindow().setBackgroundDrawableResource(R.drawable.background);
            MySurfaceView.setVisibility(View.INVISIBLE);
        }

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, MainFragment.newInstance(), "main")
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            MySurfaceView.mp.pause();
        } catch (Exception e){}
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            MySurfaceView.mp.prepare();
        } catch (Exception e) {}

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("flex", true)){
            MySurfaceView.mp.start();
        } else {
            MySurfaceView.setVisibility(View.INVISIBLE);
            getWindow().setBackgroundDrawableResource(R.drawable.background);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0)
            getSupportFragmentManager().popBackStack();
        else
            recreate();
    }
}
