package com.sportzcourt.booking.core;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.sportzcourt.booking.R;

/**
 * Created by ravindra.kambale on 12/16/2015.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance;



    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance()
    {
        return mInstance;
    }


}
