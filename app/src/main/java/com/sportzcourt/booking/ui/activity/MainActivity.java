package com.sportzcourt.booking.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.events.UserCredsEvent;
import com.sportzcourt.booking.model.response.ApiResponse;
import com.sportzcourt.booking.ui.fragment.AddVendorFragment;
import com.sportzcourt.booking.ui.fragment.HomeFragment;
import com.sportzcourt.booking.util.UIUtils;
import com.squareup.leakcanary.LeakCanary;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 * <p/>
 * <P> Main Activity to display the Home page contents of the app
 * <p/>
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 * Sunil Rao S (sunil.sindhe@happiestminds.com)
 * @created on: 4-Jan-2016
 */
public class MainActivity extends BootstrapActivity implements AddVendorFragment.VendorAddListener, HomeFragment.OnFragmentInteractionListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        LeakCanary.install(getApplication());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        showSportsList();
        Timber.tag("LifeCycles");
        Timber.i("Activity Created ");
    }


    void handleClickEvent(Button button) {
        if (button.getId() == R.id.btnSend) {
            getDashboardData();
        }
    }

    private void getDashboardData() {
        Timber.i("Sending request now");

        Call<ApiResponse> call = apiService.getDashboardData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Response<ApiResponse> response) {
                Timber.i("Received response status:%d Resp code:%s", response.code(), response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.i("Request failed");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            //Clear the sticky event and close the app.
            EventBus.getDefault().removeStickyEvent(UserCredsEvent.class);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_wheel_sample:
                                UIUtils.startActivity(MainActivity.this, WheelSampleActivity.class);
                                break;

                            case R.id.nav_home:
                                UIUtils.startActivity(MainActivity.this, CustomThemeActivity.class);
                                break;

                            case R.id.nav_add_vendor:
                                showAddVendor();
                                break;
                        }

                        return true;
                    }
                });
    }


    private void showAddVendor(){
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AddVendorFragment.newInstance(null,null)).commit();
    }

    private void showSportsList(){
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance(null, null)).commit();
    }
    @Override
    public void onVendorAdded(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
