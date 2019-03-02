package com.f0x1d.flexlauncher;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
