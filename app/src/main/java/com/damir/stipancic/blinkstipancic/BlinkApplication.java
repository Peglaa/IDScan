package com.damir.stipancic.blinkstipancic;

import android.app.Application;
import android.content.Context;

import com.microblink.MicroblinkSDK;

public class BlinkApplication extends Application {

    private static BlinkApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MicroblinkSDK.setLicenseFile("com.damir.stipancic.blinkstipancic.key", this);
        instance = this;

    }

    public static Context getAppContext(){
        return instance;
    }

}
