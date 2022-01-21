package com.damir.stipancic.blinkstipancic;

import android.app.Application;

import com.microblink.MicroblinkSDK;

public class BlinkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MicroblinkSDK.setLicenseFile("com.damir.stipancic.blinkstipancic.key", this);
    }
}
