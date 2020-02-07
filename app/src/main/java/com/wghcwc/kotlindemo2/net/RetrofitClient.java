package com.wghcwc.kotlindemo2.net;


import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.wghcwc.kotlindemo2.MyKObservableFactory;
import com.wghcwc.activitylifecycle2.ActivityLifecycle;
import com.wghcwc.mygsonconvert.MyGsonConvertFactory;
import com.wghcwc.mygsonconvert.ServiceErrorException;
import com.wghcwc.rtrofitadapter.RxJava2CallAdapterFactory;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wghcwc
 */
public class RetrofitClient {
    private final static ServiceApi mServiceApi;
    private final static KServiceApi kServiceApi;
    private final static String TAG="RetrofitClient";


    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //证书
        if (false) {
            SSLUtils.SSLParams sslParams = null;
            try {
                sslParams = SSLUtils.getSslSocketFactory(new InputStream[]{ActivityLifecycle.getApp()
                        .getAssets()
                        .open("xxx.cer")}, null, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        //log
        if (false) {
            LogInterceptor loggingInterceptor = new LogInterceptor("RetrofitClient");
            loggingInterceptor.setPrintLevel(LogInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            okHttpBuilder.addInterceptor(loggingInterceptor);
        }
        //缓存
        if (false) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/uninetcache/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            Cache cache = new Cache(file, 10L * 1024L * 1024L);
            okHttpBuilder.addInterceptor(new CacheInterceptor(604800));
            okHttpBuilder.cache(cache);
        }


        OkHttpClient okHttpClient = okHttpBuilder
                .connectTimeout(8, TimeUnit.SECONDS)
//                .addInterceptor(new HeaderIntercept())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(MyGsonConvertFactory.create((jsonObject,body) -> {
                    Log.d(TAG, "responseBody: "+body);
                    int code = Integer.parseInt(jsonObject.get("status").toString());
                    if (code == 200||code==0) {
                        return null;
                    }
                    String msg = jsonObject.get("message").toString();
                    return new ServiceErrorException(code, msg);
                }))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(MyKObservableFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mServiceApi = retrofit.create(ServiceApi.class);
        kServiceApi = retrofit.create(KServiceApi.class);

    }

    public static ServiceApi api() {
        return mServiceApi;
    }
    public static KServiceApi kApi() {
        return kServiceApi;
    }

    /**
     * RxJava1
     */
   /*  public static <T> Observable.Transformer<Result<T>, T> transformer() {
        return resultObservable -> resultObservable.flatMap(tResult -> {
            // 解析不同的情况返回
            Log.d("ss", "transformer: ");
            if (tResult.code == 0) {
                // 返回成功
                return createObservable(tResult.data);
            } else {
                // 返回失败
                return Observable.error(new ErrorHandle.ServiceError(tResult.code, tResult.msg));
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }*/

    /**
     * 成功包含确定数据,如只有msg,code,data
     */
    public static <T> ObservableTransformer<Result<T>, T> transformer() {
        return new ObservableTransformer<Result<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Result<T>> upstream) {
                return upstream.flatMap(new Function<Result<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Result<T> tResult) throws Exception {
                        Log.d("ss", "transformer: ");
                        if (tResult.status == 200 || tResult.status == 0) {
                            // 成功
                            return createObservable(tResult.data);
                        } else {
                            // 失败
                            return Observable.error(new ServiceErrorException(tResult.status, tResult.message));
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<Result<T>, Result<T>> resTransformer() {
        return new ObservableTransformer<Result<T>, Result<T>>() {
            @Override
            public ObservableSource<Result<T>> apply(Observable<Result<T>> upstream) {
                return upstream.flatMap(new Function<Result<T>, ObservableSource<Result<T>>>() {
                    @Override
                    public ObservableSource<Result<T>> apply(Result<T> tResult) throws Exception {
                        Log.d("ss", "transformer: ");
                        if (tResult.status == 200 || tResult.status == 0) {
                            // 成功
                            return createObservable(tResult);
                        } else {
                            // 失败
                            return Observable.error(new ServiceErrorException(tResult.status, tResult.message));
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 多数据,外层除了data,msg,code之外还有别的数据
     *
     * @param clazz 目标实体类,包含最外层结构
     */
    public static <T> ObservableTransformer<ResponseBody, T> baseTransformer(final Class<T> clazz) {
        return new ObservableTransformer<ResponseBody, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ResponseBody> upstream) {
                return upstream.flatMap(new Function<ResponseBody, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(ResponseBody responseBody) throws Exception {
                        try {
                            String content = new String(responseBody.bytes());
                            JSONObject object = new JSONObject(content);
                            int code = Integer.parseInt(object.get("code").toString());
                            String msg = object.get("message").toString();
                            if (code == 200) {
                                T tResult = new Gson().fromJson(content, clazz);
                                return createObservable(tResult);
                            } else {
                                // 失败
                                return Observable.error(new ServiceErrorException(code, msg));
                            }
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 创建含有数据,code,msg的Observable
     *
     * @param data data
     */
    private static <T> Observable<Result<T>> createObservable(final Result<T> data) {
        return Observable.create(new ObservableOnSubscribe<Result<T>>() {
            @Override
            public void subscribe(ObservableEmitter<Result<T>> emitter) {
                emitter.onNext(data);
                emitter.onComplete();
            }
        });
    }



    /**
     * 创建只含有数据的Observable
     *
     * @param data data
     */
    private static <T> Observable<T> createObservable(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) {
                emitter.onNext(data);
                emitter.onComplete();

            }
        });
    }

    /**
     * 创建只含有string的Observable
     *
     * @param data data
     */
    private static Observable<String> createObservable(final String data) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                emitter.onNext(data);
                emitter.onComplete();

            }
        });
    }
}
