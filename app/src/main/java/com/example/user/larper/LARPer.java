package com.example.user.larper;

import android.app.Application;
import android.content.Context;

/**
 * Created by event on 01/04/2017.
 */

public class LARPer extends Application {

    private static Context context;
    public void onCreate() {
        super.onCreate();
        LARPer.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return LARPer.context;
    }
}
