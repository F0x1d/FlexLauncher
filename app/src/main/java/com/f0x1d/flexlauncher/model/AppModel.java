package com.f0x1d.flexlauncher.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppModel {

    public String name;
    public Drawable icon;
    public Intent launch;

    public AppModel(CharSequence charSequence, Drawable icon, Intent launchIntentForPackage){
        this.name = charSequence.toString();
        this.icon = icon;
        this.launch = launchIntentForPackage;
    }
}
