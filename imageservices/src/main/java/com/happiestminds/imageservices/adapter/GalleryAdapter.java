package com.happiestminds.imageservices.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.happiestminds.imageservices.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Venkatesh.Guddanti on 1/28/2016.
 */
public class GalleryAdapter extends ArrayAdapter<String>{

    private  LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    Context context;
    ArrayList<String> labels;
    ArrayList<String> labelNames;


    public GalleryAdapter(Context context,int resource,ArrayList<String> labels) {
        super(context,resource,labels);
        this.mRandom = new Random();
        this.context = context;
        this.labels = labels;
    }
    public GalleryAdapter(Context context,int resource,ArrayList<String> labels,ArrayList<String> labelNames) {
        super(context,resource,labels);
        this.mRandom = new Random();
        this.context = context;
        this.labels = labels;
        this.labelNames = labelNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(mLayoutInflater == null)
        {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item,parent,false);
            vh = new ViewHolder();
            vh.imgView = (DynamicHeightImageView) convertView.findViewById(R.id.imgView);
            vh.labelName = (TextView) convertView.findViewById(R.id.labelName);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }
        double positionHeight = getPositionRatio(position);

        vh.imgView.setHeightRatio(positionHeight);
        Glide.with(context)
                .load(labels.get(position))
                .fitCenter()
                .into(vh.imgView);
        if(labelNames != null)
        {
            vh.labelName.setText(labelNames.get(position));
        }
        else
        {
            vh.labelName.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder
    {
       DynamicHeightImageView imgView;
        TextView labelName;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d("debug", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }


    @Override
    public int getCount() {
        return labels.size();
    }



    public static ArrayList<String> getBitmapFromGallery(String key,HashMap<String,ArrayList<String>> thumbnails) {

        return thumbnails.get(key);

    }
}
