package com.sportzcourt.booking.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.controller.DataController;
import com.sportzcourt.booking.service.webservices.ApiService;

import butterknife.ButterKnife;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Base activity for a Bootstrap activity which does not use fragments
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 *
 * @created on: 5-Jan-2016
 */
public abstract class BootstrapActivity extends AppCompatActivity {

    public ApiService apiService;

    /** The progress dialog. */
    private ProgressDialog progressDialog;
    /**
     * Class tag. Used for debug.
     */
    private String TAG;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getCanonicalName();
        apiService = DataController.getInstance().getAPIManager();
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        // Used to inject views with the Butterknife library
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // This is the home button in the top left corner of the screen.
            case android.R.id.home:
                // Don't call finish! Because activity could have been started by an
                // outside activity and the home button would not operated as expected!
                final Intent homeIntent = new Intent(this, MainActivity.class);
                //homeIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Show progress dialog.
     */
    public final void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
    }

    /**
     * Hide progress dialog.
     */
    public final void hideProgressDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
        progressDialog = null;
    }
}
