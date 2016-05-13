package com.sportzcourt.booking.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.events.UserCredsEvent;
import com.sportzcourt.booking.util.AppUtils;
import com.sportzcourt.booking.util.Constants;
import com.sportzcourt.booking.util.UIUtils;
import com.sportzcourt.booking.util.ValidationHelper;

import de.greenrobot.event.EventBus;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 * <p/>
 * <P> Splash Screen, Shows the Privacy Policy, checks for network & does
 * some basic app initialisation operations
 * <p/>
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 * Sunil Rao S (sunil.sindhe@happiestminds.com)
 * @created on: 4-Jan-2016
 */
public class SplashActivity extends Activity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        View parentLayout = findViewById(R.id.root_view);
        if (!ValidationHelper.isNetworkAvailable(this)) {
            Snackbar.make(parentLayout, "No Network Connection, Auto Exiting...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        AppUtils.registerToDefaultBus(this, true);

        //User is not logged in yet
        if (EventBus.getDefault().getStickyEvent(UserCredsEvent.class) == null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startLoginActivity();
                }

            }, Constants.App.SPLASH_TIME_IN_MILISECONDS);
        }
    }

    @Override
    protected void onStop() {
        AppUtils.unregisterFromDefaultBus(this);

        super.onStop();
    }

    private void startLoginActivity() {
        View parentLayout = findViewById(R.id.root_view);
        if (!ValidationHelper.isNetworkAvailable(this)) {
            Snackbar.make(parentLayout, "No Network Connection, Auto Exiting...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (!prefs.getBoolean(Constants.App.KEY_EULA_ACCEPTED, false)) {
                showEula();
                return;
            }
            UIUtils.startActivity(this, LoginActivity.class);
        }
        this.finish();
    }

    private void showEula() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("DISCLAIMER");
        alert.setCancelable(false);
        alert.setMessage("While we work to ensure that product information is correct, on occasion manufacturers may alter their ingredient lists. Actual product packaging and materials may contain more and/or different information than that shown on our Web site. We recommend that you do not solely rely on the information presented and that you always read labels, warnings, and directions before using or consuming a product.");
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Determine if EULA was accepted this time
                prefs.edit().putBoolean(Constants.App.KEY_EULA_ACCEPTED, true).commit();
                startLoginActivity();
            }
        });

        alert.show();
    }

    private void startMainActivity() {
        UIUtils.startActivity(this, MainActivity.class);
        this.finish();
    }

    public void onEvent(UserCredsEvent event) {
        if (event.loggedIn) {
            //User is already logged in. Launch Main Activity
            Toast.makeText(this, event.userName + " is logged in.", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            //User is not logged in
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startLoginActivity();
                }

            }, Constants.App.SPLASH_TIME_IN_MILISECONDS);
        }
    }
}
