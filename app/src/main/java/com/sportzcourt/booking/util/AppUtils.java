package com.sportzcourt.booking.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * Copyright 2015 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Generic Utility functionality
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Sunil Rao S (sunil.sindhe@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */
public class AppUtils {

    private static boolean DETAIL_LOG = true;
    private static String TAG = "YourAppName";
    private static final String VERSION = "1.0";
    private static final boolean ENABLE_LOGGER = true;
    private static long sStartTime = 0L;

    //Logger
    public static void log(String paramString) {
        if(ENABLE_LOGGER == false)
            return;

        if ((DETAIL_LOG) && (!"user".equals(Build.TYPE)))
            Log.i(TAG, getCallingMethod() + paramString);
        else
            Log.i(TAG , paramString);
    }

    public static void log(Context cxt, String paramString) {
        if(ENABLE_LOGGER == false)
            return;

        if ((DETAIL_LOG) && (!"user".equals(Build.TYPE)))
            Log.i(TAG + getAppVersion(cxt), getCallingMethod() + paramString);
        else
            Log.i(TAG + getAppVersion(cxt), paramString);
    }

    private static String getAppVersion(Context cxt) {
        PackageInfo pInfo  = null;

        if(cxt == null)
            return VERSION;

        try {
            pInfo = cxt.getPackageManager().getPackageInfo(cxt.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    public static String getCallingMethod() {
        StringBuffer localStringBuffer = new StringBuffer();
        StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
        for (int i = 0; ; i++)
        {
            if (i >= arrayOfStackTraceElement.length);
            do
            {
                int j = i + 2;
                if (j <= arrayOfStackTraceElement.length)
                {
                    String str1 = arrayOfStackTraceElement[j].getClassName();
                    String str2 = str1.substring(1 + str1.lastIndexOf('.'));
                    localStringBuffer.append("[").append(str2).append(".")
                            .append(arrayOfStackTraceElement[j].getMethodName())
                            .append("()#").append(arrayOfStackTraceElement[j].getLineNumber())
                            .append("] ");
                }
                return localStringBuffer.toString();
            }
            while (arrayOfStackTraceElement[i].getClassName().equals(AppUtils.class.getName()));
        }
    }

    //Database
    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            if (!cursor.isClosed())
                cursor.close();
            return;
        }
    }

    //Performance Start End time
    public static void startTimeTrack() {
        sStartTime = System.currentTimeMillis();
    }

    public static void endTimeTrack(String paramString) {
        if (sStartTime == 0L)
            log("Incorrect endTimeTrack, make sure to call startTimeTrack!");
        else {
            log(paramString + " execution_time " + (System.currentTimeMillis() - sStartTime) + " ms");
            sStartTime = 0L;
        }
    }

    /**
     * Method to register to the default EventBus if not already registered.
     *
     * @param object   Object to be registered
     * @param isSticky Whether to register to sticky events or not
     */
    public static void registerToDefaultBus(Object object, boolean isSticky) {

        if (object == null)
            return;

        if (EventBus.getDefault().isRegistered(object))
            //Already registered
            return;

        if (isSticky)
            EventBus.getDefault().registerSticky(object);
        else
            EventBus.getDefault().register(object);
    }

    /**
     * Method to unregister from the default EventBus if registered.
     *
     * @param object Object to be unregistered
     */
    public static void unregisterFromDefaultBus(Object object) {

        if (object == null)
            return;

        if (!EventBus.getDefault().isRegistered(object))
            //Not registered
            return;

        EventBus.getDefault().unregister(object);
    }
}
