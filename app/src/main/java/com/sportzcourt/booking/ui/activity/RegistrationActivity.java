package com.sportzcourt.booking.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.data.RegisterData;

import java.io.InputStream;
import java.net.URL;
/**
 * Created by Abhishek.Chandale on 1/11/2016.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    TextView userName;
    TextView userDetail;
    ImageView userimage;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        btn=(Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(this);
        SharedPreferences.Editor editor = getSharedPreferences("button_state", MODE_PRIVATE).edit();
        UserInfo();

    }
    public void UserInfo(){
        userName=(TextView)findViewById(R.id.user_name);
        userDetail=(TextView)findViewById(R.id.txt_info);
        userimage=(ImageView)findViewById(R.id.user_image);
        userName.setText(RegisterData.getInstance().getName());
        userDetail.setText("Id:" + RegisterData.getInstance().getId() +
                "\nName:" + RegisterData.getInstance().getName() +
                "\nEmail:" + RegisterData.getInstance().getEmail() +
                "\nProfile-Info:" + RegisterData.getInstance().getAboutMe() +
                "\nDOB:" + RegisterData.getInstance().getBirthday() +
                "\nGender:" + RegisterData.getInstance().getGender());
        new LoadImageData().execute(RegisterData.getInstance().getImage());
    }

    @Override
    public void onClick(View v) {
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

    }

    private class LoadImageData extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                InputStream in = url.openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                        /* TODO log error */
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            userimage.setImageBitmap(bitmap);
            // progressDialog.cancel();
        }
    }

}
