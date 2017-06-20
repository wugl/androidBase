package com.steelkiwi.cropiwa;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author yarolegovich https://github.com/yarolegovich
 * 25.02.2017.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(getApplicationContext());
    }

    public static App getInstance() {
        return instance;
    }
}
