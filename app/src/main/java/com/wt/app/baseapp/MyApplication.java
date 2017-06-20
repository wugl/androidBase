package com.wt.app.baseapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by wuguilin on 6/15/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
    }
}
