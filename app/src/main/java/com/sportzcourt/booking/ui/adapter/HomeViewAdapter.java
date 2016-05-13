package com.sportzcourt.booking.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.data.Sport;
import com.sportzcourt.booking.ui.adapter.viewholder.HomeViewHolder;

import java.util.List;

/**
 * Copyright 2016 (C) 10i Commerce pvt ltd.
 * <p>
 * <P> Description :
 * <p>
 * <P>Notes:
 * <P>Dependency:
 *
 * @author User
 * @created on: 3/27/2016
 */
public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private List<Sport> sportsList;
    private Context context;
    private String retailerName;
    private DisplayImageOptions options;
    private String retailerStore;
    private ImageLoader imageLoader;


    public HomeViewAdapter(Context context, List<Sport> categories) {
        this.sportsList = categories;
        this.context = context;

    }

    public void setData(List<Sport> categories){
        this.sportsList = categories;
    }
    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_view, null);
        HomeViewHolder rcv = new HomeViewHolder(layoutView, sportsList, context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
       // holder.countryName.setText(sportsList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return this.sportsList.size();
    }
}