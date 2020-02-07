package com.wghcwc.kotlindemo2.net.observer;


import android.util.Log;

import com.google.gson.JsonParseException;
import com.wghcwc.everyshowing.LoadingHelper;
import com.wghcwc.everyshowing.toast.MyToast;
import com.wghcwc.mygsonconvert.ServiceErrorException;

import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * @author wghcwc
 * 网络用
 */
public abstract class BaseObserver<T> implements Observer<T> {
    private static final String TAG = "OkHttpError";

    protected BaseObserver() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (t == null) {
            onError(new NullPointerException());
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onComplete() {
        LoadingHelper.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ServiceErrorException) {
            ServiceErrorException serviceError = (ServiceErrorException) e;
//            if (serviceError.errorCode == 211 || serviceError.errorCode == 212) {
//                onComplete();
//                return;
//            }
            onError(serviceError.errorCode, serviceError.getMessage());
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            onError(httpException.code(), "服务器异常");
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            onError(500, "解析错误");
        } else if (e instanceof ConnectException) {
            onError(400, "连接错误");
        } else if (e instanceof NullPointerException) {
            onError(500, "数据为空");
        } else if (e instanceof UnknownHostException) {
            onError(400, "无网络连接");
        } else if (e instanceof SocketTimeoutException) {
            onError(400, "链接超时");
        } else if (e instanceof SocketException) {
            onError(500, "链接关闭");
        } else if (e instanceof EOFException) {
            onError(500, "链接关闭");
        } else if (e instanceof IllegalArgumentException) {
            onError(400, "参数错误");
        } else if (e instanceof SSLException) {
            onError(500, "证书错误");
        } else {
            onError(500, "未知错误");
        }
        Log.e(TAG, "onError: " + e.toString());
        onComplete();
    }

    /**
     * 成功回调
     *
     * @param t data
     */
    protected abstract void onSuccess(T t);

    /**
     * 错误回调
     *
     * @param errorCode    code
     * @param errorMessage msg
     */
    protected void onError(int errorCode, String errorMessage) {
        MyToast.show(errorMessage);
    }

}
