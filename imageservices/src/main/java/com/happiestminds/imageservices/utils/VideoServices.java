package com.happiestminds.imageservices.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.happiestminds.imageservices.model.VideoCaptureType;
import com.happiestminds.imageservices.ui.GalleryActivityThumbs;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;


/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Component for VideoCapture/Downloading
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Venkatesh Guddanti (Venkatesh.Guddanti@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */
public final class VideoServices {

    public static void captureVideo(Activity activity,VideoCaptureType type)
    {
        switch (type) {
            case CAPTURE_BY_CAMERA:
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(cameraIntent.resolveActivity(activity.getPackageManager()) != null)
                activity.startActivityForResult(cameraIntent,type.getVideoCaptureType());
                break;

            case CAPTURE_BY_GALLERY:

                Intent i = new Intent(activity, GalleryActivityThumbs.class);
                i.putExtra(Constants.Gallery.GALLARY_TYPE, Constants.Gallery.GALLARY_TYPE_VIDEO);
                i.putExtra(Constants.Gallery.GALLARY_MODE, Constants.Gallery.GALLARY_GROUP);
                activity.startActivityForResult(i, type.getVideoCaptureType());
                break;
            case CAPTURE_WITH_COMPRESS:

                 i = new Intent(activity, GalleryActivityThumbs.class);
                i.putExtra("TYPE", "VIDEO");
                activity.startActivityForResult(i, type.getVideoCaptureType());

                break;

        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;

        if(contentUri == null)
        {
            return "";
        }
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            Log.d("debug","url from"+cursor.getString(column_index));
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
    public static void downloadVideoFromUrl(final Activity activity,String url,final ProgressDialog mProgressDialog,final ImageView imageThumbnail)
    {

        mProgressDialog.setMessage("A message");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.show();

        final String path = Environment.getExternalStorageDirectory() + File.separator + "video.mp4";

        FileDownloader.getImpl().create(url)
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        mProgressDialog.setProgress(1 + (int) ((soFarBytes * 100) / totalBytes));
                        Log.d("debug", "progress" + soFarBytes + "," + "total bytes" + totalBytes);

                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

                        Toast.makeText(activity, "File Downloaded Successfully", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                        if (imageThumbnail != null) {
                            Bitmap thumbNail;
                            thumbNail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                            imageThumbnail.setImageBitmap(thumbNail);
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {


                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }


}
