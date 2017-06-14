package com.ody.library.base;

import android.app.Application;
import android.os.StrictMode;

import com.ody.library.BuildConfig;
import com.ody.library.util.util.LogUtils;
import com.ody.library.util.util.Utils;

/**
 * Created by Samuel on 2017/5/4.
 */

public class BaseApplication extends Application  {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);


        LogUtils.Builder logBuilder = new LogUtils.Builder();
        if (BuildConfig.IS_DEBUG) {
            logBuilder.setLogSwitch(true);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
        } else {
            logBuilder.setLogSwitch(true);
        }
    }


}
