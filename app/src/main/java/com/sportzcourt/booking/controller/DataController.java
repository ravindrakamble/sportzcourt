package com.sportzcourt.booking.controller;

import android.content.Context;

import com.sportzcourt.booking.BuildConfig;
import com.sportzcourt.booking.database.DatabaseManager;
import com.sportzcourt.booking.service.webservices.ApiService;
import com.sportzcourt.booking.service.webservices.RestAdapterRequestInterceptor;
import com.sportzcourt.booking.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * Created by ravindra.kambale on 11/30/2015.
 */
public class DataController {
    private static DataController dcInstance = new DataController();
    private DatabaseManager dbManager;
    private ApiService apiManager;
    public static DataController getInstance() {
        return dcInstance;
    }

    private DataController() {
    }

    public DatabaseManager getDatabaseManager(Context context){
        if(dbManager == null){
            dbManager = DatabaseManager.getInstance(context);
        }

        return dbManager;
    }

    public ApiService getAPIManager(){
        if(apiManager == null){
            Retrofit retrofit = provideRestAdapter(provideRestAdapterRequestInterceptor());
            apiManager = retrofit.create(ApiService.class);
        }

        return apiManager;
    }

    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor() {
        return new RestAdapterRequestInterceptor();
    }

    Retrofit provideRestAdapter(RestAdapterRequestInterceptor restRequestInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(Constants.Http.READ_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(Constants.Http.CONNECT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(provideRestAdapterRequestInterceptor());

        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(Constants.Http.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
