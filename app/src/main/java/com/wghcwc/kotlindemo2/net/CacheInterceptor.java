package com.wghcwc.kotlindemo2.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.wghcwc.activitylifecycle2.ActivityLifecycle;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存
 */
public class CacheInterceptor implements Interceptor {
    private int maxStale;

    public CacheInterceptor(int maxStale) {
        this.maxStale = maxStale;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkConnected(ActivityLifecycle.getApp())) {
            CacheControl tempCacheControl = new CacheControl.Builder().maxStale(maxStale, TimeUnit.SECONDS).build();
            request = request.newBuilder()
                    .cacheControl(tempCacheControl)
                    .build();
        }
        return chain.proceed(request);
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
