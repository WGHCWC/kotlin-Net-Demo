package com.wghcwc.kotlindemo2.net.observer

import android.util.Log
import com.google.gson.JsonParseException
import com.wghcwc.everyshowing.LoadingHelper
import com.wghcwc.everyshowing.toast.MyToast
import com.wghcwc.mygsonconvert.ServiceErrorException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import org.json.JSONException
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @author wghcwc
 * 网络用
 */
 open class KBaseObserver<T> constructor(private val next1: (t:T) -> Unit): Observer<T?> {


    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        if (t == null) {
            onError(NullPointerException())
        } else {
            next1(t)
        }
    }

    override fun onComplete() {
        LoadingHelper.dismiss()
    }

    override fun onError(e: Throwable) {
        if (e is ServiceErrorException) {
            val serviceError = e
            if (serviceError.errorCode == 211 || serviceError.errorCode == 212) {
                onComplete()
                return
            }
            onError(serviceError.errorCode, serviceError.message)
        } else if (e is HttpException) {
            onError(e.code(), "服务器异常")
        } else if (e is JsonParseException || e is JSONException) {
            onError(500, "解析错误")
        } else if (e is ConnectException) {
            onError(400, "连接错误")
        } else if (e is NullPointerException) {
            onError(500, "数据为空")
        } else if (e is UnknownHostException) {
            onError(400, "无网络连接")
        } else if (e is SocketTimeoutException) {
            onError(400, "链接超时")
        } else if (e is SocketException) {
            onError(500, "链接关闭")
        } else if (e is EOFException) {
            onError(500, "链接关闭")
        } else if (e is IllegalArgumentException) {
            onError(400, "参数错误")
        } else if (e is SSLException) {
            onError(500, "证书错误")
        } else {
            onError(500, "未知错误")
        }
        Log.e(TAG, "onError: $e")
        onComplete()
    }

    /**
     * 错误回调
     *
     * @param errorCode    code
     * @param errorMessage msg
     */
     fun onError(errorCode: Int, errorMessage: String?) {
        MyToast.show(errorMessage)
    }

    companion object {
        private const val TAG = "OkHttpError"
    }
}