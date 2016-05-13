package com.happiestminds.imageservices.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.happiestminds.imageservices.model.ImageCaptureType;
import com.happiestminds.imageservices.ui.GalleryActivityThumbs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Component for imagecapture/cropping/Downloading
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Venkatesh Guddanti (Venkatesh.Guddanti@happiestminds.com)
 *          Barun Gupta (barun.gupta@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */
public final class ImageServices {



    public static final int HANDLE_CROP_INTENT = 1010;

    public static void displayImageFromUrl(Context context,String url,ImageView image)
    {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(image);
    }


    public static void imageCapture(Activity context,ImageCaptureType type)
    {
        switch (type) {
            case CAPTURE_BY_CAMERA:
                context.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), type.getImageCaptureType());
                break;
            case CAPTURE_BY_CAMERA_WITHCROP:
                context.startActivityForResult(getPickImageChooserIntent(context), type.getImageCaptureType());
                break;
            case CAPTURE_BY_GALLERY:
                Intent i = new Intent(context, GalleryActivityThumbs.class);
                i.putExtra(Constants.Gallery.GALLARY_TYPE, Constants.Gallery.GALLARY_TYPE_IMAGE);
                context.startActivityForResult(i, type.getImageCaptureType());
                break;
            case CAPTURE_BY_GALLERY_WITHCROP:
                Intent galleryintent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                context.startActivityForResult(galleryintent, type.getImageCaptureType());
                break;
            case CAPTURE_BY_CAMERA_ORIGINAL:


                break;
        }

    }


    public static Bitmap getBitmapFromCameraIntent(Intent data)
    {
        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // convert byte array to Bitmap

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                byteArray.length);
        return bitmap;

    }
    public static Bitmap getBitmapFromGalleryIntent(Context context,Intent data)
    {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return  BitmapFactory.decodeFile(picturePath);

    }

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }


    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public static Intent getPickImageChooserIntent(Context context) {
// Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri(context);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
    /**
     * Get URI to image received from capture by camera.
     */
    public static Uri getCaptureImageOutputUri(Context context) {
        Uri outputFileUri = null;
        File getImage = context.getExternalCacheDir();
        if(getImage.exists())
        {
            getImage.delete();
        }
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public static Uri getPickImageResultUri(Intent data,Activity activity) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri(activity) : data.getData();
    }

}
