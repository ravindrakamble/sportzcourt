package com.sportzcourt.booking.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by ravindra.kambale on 9/1/2015.
 */
public final class UIUtils {
    static InputMethodManager manager;



    /**
     * Finish the given activity and start a home activity class.
     * <p>
     * This mirror the behavior of the home action bar button that clears the
     * current activity and starts or brings another activity to the top.
     *
     * @param activity
     * @param homeActivityClass
     */
    public static void goHome(Activity activity, Class<?> homeActivityClass) {
        Intent intent = new Intent(activity, homeActivityClass);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    /**
     * Hide soft input method manager
     *
     * @param view
     * @return view
     */
    public static View hideSoftInput(final Context context, final View view) {
        Timber.i("hideSoftInput :" + manager);
        if (manager == null){
            manager =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    public static void startActivity(final Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }



}
