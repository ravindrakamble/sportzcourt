package com.sportzcourt.booking.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.sportzcourt.booking.R;

public class CustomThemeChangeUtils {

    public static String SIZE = "";
    public static boolean settingChanged = false;
    public static String THEME = "";

    public static void changeToTheme(Activity activity) {

        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void setThemeToActivity(Activity act) {

        try {
            if (CustomThemeChangeUtils.THEME.equalsIgnoreCase("default")) {
                act.setTheme(R.style.Theme_DefaultTheme);
                Log.d("", "Default theme is to be applied.");
            }


            if (CustomThemeChangeUtils.THEME.equalsIgnoreCase("Gray")) {
                act.setTheme(R.style.Theme_Gray);
                Log.d("", "gray theme is to be applied.");
            }

            if (CustomThemeChangeUtils.THEME.equalsIgnoreCase("Radial")) {
                act.setTheme(R.style.Theme_Radial);
                Log.d("", "radial theme is to be applied.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}