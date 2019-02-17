package com.f0x1d.flexlauncher.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.f0x1d.flexlauncher.R;

import java.io.IOException;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public static MediaPlayer mp = new MediaPlayer();
    public static boolean hasActiveHolder = true;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);

        Log.e("flex", "surface view constructor");
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        Log.e("flex", "surface view constructor");
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);

        Log.e("flex", "surface view constructor");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setFitsSystemWindows(true);

        if (mp == null)
            mp = new MediaPlayer();

        try {
            AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.flex);
            if (afd == null) return;

            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
        } catch (Exception e){
            //Log.e("flex", e.getLocalizedMessage());
        }

        android.view.ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = (int) (getHeight() * (640f/360f));
        setLayoutParams(lp);

        setX(-(lp.width - getResources().getDisplayMetrics().widthPixels)/2);

        Log.e("flex", "widthpixels: " + getResources().getDisplayMetrics().widthPixels);
        Log.e("flex", "getWidth: " + getWidth());
        Log.e("flex", "lp width: " + lp.width);

        mp.setDisplay(holder);
        mp.setLooping(true);
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("flex", true)){
            mp.seekTo(20500);
            mp.start();
        }

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("flex", true)){
                    mp.start();
                }
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            mp.setDisplay(null);
        } catch (Exception e){}
    }
}