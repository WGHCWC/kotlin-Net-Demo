package com.wghcwc.kotlindemo2.net

import com.wghcwc.kotlindemo2.CObservable
import com.wghcwc.kotlindemo2.KObservable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.SkipCallbackExecutor
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @author wghcwc
 */
interface KServiceApi {
    @GET
    fun get(@Url url: String?): CObservable<ResponseBody>
    @GET
    fun getC(@Url url: String?): CObservable<ResponseBody>

    @GET
    suspend fun getsu(@Url url: String?): ResponseBody

    @GET
     fun getCall(@Url url: String?): Call<ResponseBody>

    @GET
     fun getDsu(@Url url: String?): Deferred<ResponseBody>
}