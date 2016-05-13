/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sportzcourt.booking.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happiestminds.imageservices.model.ImageCaptureType;
import com.happiestminds.imageservices.utils.Constants;
import com.happiestminds.imageservices.utils.ImageServices;
import com.happiestminds.imageservices.utils.VideoServices;
import com.sportzcourt.booking.R;
import com.sportzcourt.booking.ui.adapter.Items;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> To display the details of the selected items, Using the Collapsible
 * toolbar & the card view & recycler view components from material design
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Sunil Rao S (sunil.sindhe@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */
public class ItemDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "cheese_name";
    Uri imageUri = null;

     ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String itemName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(itemName);

        loadBackdrop();
    }


    public void onButtonClick(View v)
    {
        //Crop.pickImage(this);

        final CharSequence[] items = {
                ImageCaptureType.CAPTURE_BY_CAMERA.toString(), ImageCaptureType.CAPTURE_BY_CAMERA_WITHCROP.toString(),
                ImageCaptureType.CAPTURE_BY_GALLERY.toString(),ImageCaptureType.CAPTURE_BY_GALLERY_WITHCROP.toString()
                ,"Download Image",ImageCaptureType.CAPTURE_BY_CAMERA_ORIGINAL.toString()
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        ImageServices.imageCapture(ItemDetailActivity.this, ImageCaptureType.CAPTURE_BY_CAMERA);
                        break;
                    case 1:
                        ImageServices.imageCapture(ItemDetailActivity.this, ImageCaptureType.CAPTURE_BY_CAMERA_WITHCROP);
                        break;
                    case 2:
                        //Will return image path to activity result by resultcode ImageCaptureType.CAPTURE_BY_GALLERY
                        ImageServices.imageCapture(ItemDetailActivity.this, ImageCaptureType.CAPTURE_BY_GALLERY);
                        break;
                    case 3:
                        ImageServices.imageCapture(ItemDetailActivity.this, ImageCaptureType.CAPTURE_BY_GALLERY_WITHCROP);
                        break;
                    case 4:
                        ImageServices.displayImageFromUrl(ItemDetailActivity.this,"http://farm6.staticflickr.com/5344/9049177018_4621cb63db_s.jpg", imageView);
                        break;
                    case 5:
                        imageUri = ImageServices.getOutputMediaFileUri();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, ImageCaptureType.CAPTURE_BY_CAMERA_ORIGINAL.getImageCaptureType());
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void loadBackdrop() {
        imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Items.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && data!=null ) {
            if(requestCode == ImageCaptureType.CAPTURE_BY_GALLERY_WITHCROP.getImageCaptureType()) {
                try {
                    Intent cropIntent = new Intent(this,ImageCropActivity.class);
                    Uri imageUri = ImageServices.getCaptureImageOutputUri(this);
                    cropIntent.putExtra("imageUri", imageUri.toString());
                    startActivityForResult(cropIntent, ImageServices.HANDLE_CROP_INTENT);
                }
                catch(ActivityNotFoundException aNFE){
                    //display an error message if user device doesn't support
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
            else if(requestCode == ImageCaptureType.CAPTURE_BY_CAMERA.getImageCaptureType())
            {
                imageView.setImageBitmap(ImageServices.getBitmapFromCameraIntent(data));
                Log.d("debug", "data uri" + VideoServices.getRealPathFromURI(this, data.getData()));
            }
            else if(requestCode==ImageCaptureType.CAPTURE_BY_CAMERA_WITHCROP.getImageCaptureType()) {
            try {
                Intent cropIntent = new Intent(this, ImageCropActivity.class);
                Uri imageUri = ImageServices.getCaptureImageOutputUri(this);
                cropIntent.putExtra("imageUri", imageUri.toString());
                startActivityForResult(cropIntent, ImageServices.HANDLE_CROP_INTENT);
            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        }
        if(requestCode==ImageServices.HANDLE_CROP_INTENT && data != null){

            String imgpath =data.getStringExtra("img");
            if(imgpath != null){
                Bitmap bmImg = BitmapFactory.decodeFile(imgpath);
                imageView.setImageBitmap(bmImg);
            }else {
                Toast.makeText(getApplicationContext(), "Cannot load the image", Toast.LENGTH_LONG).show();
             }
        } else if(requestCode == ImageCaptureType.CAPTURE_BY_CAMERA_ORIGINAL.getImageCaptureType() && data != null) {

            Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .into(imageView);
        }
        else if(requestCode == ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType() && data != null) {
            Glide.with(this)
                    .load(data.getExtras().getString(Constants.Gallery.IMAGE_PATH))
                    .fitCenter()
                    .into(imageView);
        }


    }
}
