package com.sportzcourt.booking.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Base class for all Bootstrap Activities that need fragments.
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */

public class BootstrapFragmentActivity extends FragmentActivity {

    protected EventBus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = EventBus.getDefault();
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);
        //Load for dependency injection
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }
}
