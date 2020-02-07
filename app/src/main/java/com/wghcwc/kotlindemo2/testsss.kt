package com.wghcwc.kotlindemo2

import com.wghcwc.kotlindemo2.MainActivity.Companion.baidu
import com.wghcwc.kotlindemo2.net.RetrofitClient
import com.wghcwc.kotlindemo2.net.observer.KBaseObserver
import okhttp3.ResponseBody

/**
 * @author wghcwc
 * @date 20-2-1
 */
internal class testsss {
    fun aaa() {
        RetrofitClient.api()[baidu]
            .subscribe(object :
                KBaseObserver<ResponseBody?>({ responseBody: ResponseBody? -> null }) {
                override fun onError(e: Throwable) {
                    super.onError(e)
                }
            }
            )
    }

    companion object {
        var baid = "ss"
    }
}