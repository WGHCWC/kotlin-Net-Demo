package com.wghcwc.kotlindemo2

import android.util.Log.d
import android.util.Log.e
import com.wghcwc.kotlindemo2.net.RetrofitClient
import com.wghcwc.kotlindemo2.net.observer.BaseObserver
import com.wghcwc.kotlindemo2.net.observer.KBaseObserver
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import kotlin.concurrent.thread

/**
 *@author wghcwc
 *@date 20-2-3
 */
class MainViewModel : BaseMainViewModel() {

    fun get() {
        launch {
            RetrofitClient.kApi().getsu(MainActivity.baidu)
        }
    }

    fun getTest() {
        launch {
            d(TAG, "start net")
            RetrofitClient.kApi().getCall(MainActivity.baidu).subscribe(this) { _, t ->
                e("MainViewModel", t)
            }?.let {
                d("MainViewModel", it.toString())
            }
        }
    }



    /**
     * rxJava
     * */
    fun getsu3() {
        RetrofitClient.api().get(MainActivity.baidu).subscribe(KBaseObserver { res ->
            d(TAG, res.string())
        })
    }

    val can = fun() {

    }

}