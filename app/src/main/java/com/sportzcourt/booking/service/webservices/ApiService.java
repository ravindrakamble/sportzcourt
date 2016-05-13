package com.sportzcourt.booking.service.webservices;

import com.sportzcourt.booking.model.response.ApiResponse;
import com.sportzcourt.booking.model.request.LoginData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ravindra.kambale on 8/28/2015.
 */
public interface ApiService {
    @POST("test/v1/login")
    Call<ApiResponse> login(@Body LoginData loginData);

    @POST("test/v1/getUsername")
    Call<ApiResponse> retrivePassword(@Body LoginData loginData);

    @GET("test/v1/getDashboardData")
    Call<ApiResponse> getDashboardData();
}
