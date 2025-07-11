package com.study.sunnyweather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class SunnyWeatherApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    // 彩云API — TOKEN
    public static final String TOKEN = "3EqqNbg17E4tFHJ1";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
    public static Context getContext() {
        return context;
    }
}
