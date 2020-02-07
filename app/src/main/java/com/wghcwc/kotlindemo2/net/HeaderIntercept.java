package com.wghcwc.kotlindemo2.net;


import com.wghcwc.kotlindemo2.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wghcwc
 */
public class HeaderIntercept implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        Headers.Builder headerBuilder = request.headers().newBuilder();

        request = requestBuilder.build();
        return chain.proceed(request);
    }
}