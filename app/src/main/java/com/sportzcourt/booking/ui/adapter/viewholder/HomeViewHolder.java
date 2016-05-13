package com.sportzcourt.booking.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportzcourt.booking.model.data.Sport;

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
public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView countryName;
    public ImageView countryPhoto;
    public List<Sport> sportsList;
    public Context context;
    /*public TextView txtRetailerName;
    public TextView txtRetailerStore;
    public RelativeLayout rlRetailer;
*/
    public HomeViewHolder(View itemView, List<Sport> categoryList, Context context) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.sportsList = categoryList;
        this.context = context;
        //countryName = (TextView) itemView.findViewById(R.id.country_name);
        //countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {

    }
}