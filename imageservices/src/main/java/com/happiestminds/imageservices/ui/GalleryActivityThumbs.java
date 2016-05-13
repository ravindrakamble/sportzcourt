package com.happiestminds.imageservices.ui;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.View;

import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.happiestminds.imageservices.R;
import com.happiestminds.imageservices.adapter.GalleryAdapter;
import com.happiestminds.imageservices.model.ImageCaptureType;
import com.happiestminds.imageservices.utils.Constants;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> GallaryActivtyThumb which used for create its own gallery from device folders along with names(all pictures)
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Venkatesh Guddanti (Venkatesh.Guddanti@happiestminds.com)
 *
 * @created on: 28-Jan-2016
 */
public class GalleryActivityThumbs extends AppCompatActivity implements AbsListView.OnScrollListener,AbsListView.OnItemClickListener{

    ArrayList<String> catogoriesNames = new ArrayList<String>();
    ArrayList<String> catogoryThumbnails = new ArrayList<String>();
    public static Map<String,ArrayList<String>> galleryItems = new HashMap<>();
    public static String Type = "";


    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_with_thumbs);

        setTitle("GALLERY");
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        Type = getIntent().getExtras().getString(Constants.Gallery.GALLARY_TYPE);

        String[] projection;
        if(Type.equalsIgnoreCase(Constants.Gallery.GALLARY_TYPE_IMAGE)) {

             projection = new String[]{
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATA
            };
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            Cursor cur = getContentResolver().query(images,projection,null,null,null);

            if (cur.moveToFirst()) {
                String bucket;
                int bucketColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);



                do {
                    // Get the field values
                    bucket = cur.getString(bucketColumn);

                    if(!catogoriesNames.contains(bucket))
                    {
                        createBucketImage(bucket);
                        catogoriesNames.add(bucket);
                    }

                    // Do something with the values.

                } while (cur.moveToNext());



            }
        }
        else if(Type.equalsIgnoreCase(Constants.Gallery.GALLARY_TYPE_VIDEO))
        {
            projection = new String[]{
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.DATA
            };
            Uri videos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            Cursor cur = getContentResolver().query(videos,projection,null,null,null);

            if (cur.moveToFirst()) {
                String bucket;
                int bucketColumn = cur.getColumnIndex(
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME);


                do {
                    bucket = cur.getString(bucketColumn);

                    if(!catogoriesNames.contains(bucket))
                    {
                        createBucketVideo(bucket);
                        catogoriesNames.add(bucket);
                    }

                    // Do something with the values.
                    Log.d("debug", " bucket=" + catogoriesNames.size()+","+bucket);
                } while (cur.moveToNext());

            }
        }

        Log.d("debug", " bucketMapSize=" + galleryItems.size());

        for(int i=0 ; i <= catogoriesNames.size()-1;i++)
        {
            catogoryThumbnails.add(getBitmapFromGallery(catogoriesNames.get(i)).get(0));
            Log.d("debug", " bucketMapSize=" + catogoryThumbnails.get(i));
        }




        mAdapter = new GalleryAdapter(this,android.R.layout.simple_list_item_1,
                catogoryThumbnails,catogoriesNames);


        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);



    }

    private void createBucketImage(String bucketName) {

        ArrayList<String> catogoriesThumbnails = new ArrayList<String>();
       String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = getContentResolver().query(images,projection,projection[0]+"=?",new String[]{bucketName},null);

        Log.d("debug", " bucket=" + bucketName);

        if(cur.moveToFirst())
        {
            String name;
            int column =  cur.getColumnIndex(MediaStore.Images.Media.DATA);

            do {
               name = cur.getString(column);
                Log.d("debug", " bucket="+bucketName+"  ,  "+name);
                catogoriesThumbnails.add(name);
               }while (cur.moveToNext());
            galleryItems.put(bucketName, catogoriesThumbnails);
        }
        cur.close();


    }
    private void createBucketVideo(String bucketName) {

        ArrayList<String> catogoriesThumbnails = new ArrayList<String>();
        String[] projection = new String[]{
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = getContentResolver().query(images,projection,projection[0]+"=?",new String[]{bucketName},null);

        Log.d("debug", " bucket=" + bucketName);

        if(cur.moveToFirst())
        {
            String name;
            int column =  cur.getColumnIndex(MediaStore.Video.Media.DATA);

            do {
                name = cur.getString(column);
                Log.d("debug", " bucket="+bucketName+"  ,  "+name);
                catogoriesThumbnails.add(name);
            }while (cur.moveToNext());
            galleryItems.put(bucketName, catogoriesThumbnails);
        }
        cur.close();


    }




    public static ArrayList<String> getBitmapFromGallery(String key) {

        return galleryItems.get(key);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType() && data != null)
        {

            setResult(ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType(), data);
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, GalleryActivityItems.class);
        i.putStringArrayListExtra(Constants.Gallery.GALLARY_ITEMS, getBitmapFromGallery(catogoriesNames.get(position)));
        startActivityForResult(i, ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType());

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
