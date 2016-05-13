package com.sportzcourt.booking.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sportzcourt.booking.R;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> A placeholder fragment containing a simple view.
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */
public class MainActivityFragment extends Fragment {


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_main, container, false);

    }
}
