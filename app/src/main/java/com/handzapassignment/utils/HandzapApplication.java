package com.handzapassignment.utils;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.handzapassignment.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class HandzapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
