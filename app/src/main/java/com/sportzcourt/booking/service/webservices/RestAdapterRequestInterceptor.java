package com.sportzcourt.booking.service.webservices;

import com.sportzcourt.booking.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ravindra.kambale on 8/28/2015.
 */
public class RestAdapterRequestInterceptor implements Interceptor {

    public RestAdapterRequestInterceptor() {
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().
                addHeader(Constants.Http.CONTENT_TYPE, Constants.Http.CONTENT_TYPE_JSON)
                .build();
        return chain.proceed(request);
    }
}
