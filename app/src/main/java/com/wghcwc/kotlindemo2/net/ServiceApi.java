package com.wghcwc.kotlindemo2.net;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;


/**
 * @author wghcwc
 */
public interface ServiceApi {
    @GET()
     Observable<ResponseBody> get(@Url String url);

}
