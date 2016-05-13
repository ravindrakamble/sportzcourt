package com.happiestminds.imageservices.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.happiestminds.imageservices.R;
import com.happiestminds.imageservices.adapter.GalleryAdapter;
import com.happiestminds.imageservices.model.ImageCaptureType;
import com.happiestminds.imageservices.utils.Constants;

import java.util.ArrayList;


/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> GallaryActivtyItem which is used to show specific group items
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Venkatesh Guddanti (Venkatesh.Guddanti@happiestminds.com)
 *
 * @created on: 28-Jan-2016
 */
public class GalleryActivityItems extends AppCompatActivity implements AbsListView.OnItemClickListener,AbsListView.OnScrollListener {

    ArrayList<String> imageItems = new ArrayList<String>();
    GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_with_thumbs);
        setTitle("GALLERY ITEM LIST");
        StaggeredGridView gallery = (StaggeredGridView)findViewById(R.id.grid_view);
        imageItems = getIntent().getStringArrayListExtra(Constants.Gallery.GALLARY_ITEMS);
        mAdapter = new GalleryAdapter(this,android.R.layout.simple_list_item_1,
                imageItems);




        gallery.setAdapter(mAdapter);
        gallery.setOnScrollListener(this);
        gallery.setOnItemClickListener(this);


    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        intent.putExtra(Constants.Gallery.IMAGE_PATH,imageItems.get(position));
        setResult(ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType(), intent);
        finish();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
