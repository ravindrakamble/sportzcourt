package com.sportzcourt.booking.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.request.LoginData;
import com.sportzcourt.booking.model.response.ApiResponse;
import com.sportzcourt.booking.util.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Forgot password functionality
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 *
 * @created on: 1-Jan-2016
 */
public class ForgotPasswordActivity extends BootstrapActivity {

    @Bind(R.id.et_forgot_password)
    EditText etForgotPassword;

    @Bind(R.id.btn_forgotPassword)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    @OnClick(R.id.btn_forgotPassword)
    public void handleButtonClick(){
        LoginData loginData = new LoginData();
        loginData.setUsername(etForgotPassword.getText().toString().trim());
        Call<ApiResponse> call = apiService.retrivePassword(loginData);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Response<ApiResponse> response) {
                Timber.i("Retrieving username successful");
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.i("Retrieving username failed");
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void showLoginActivity(){
        UIUtils.startActivity(this, LoginActivity.class);
        this.finish();
    }
}
