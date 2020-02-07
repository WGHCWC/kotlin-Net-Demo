package com.wghcwc.kotlindemo2

import android.util.Log
import com.wghcwc.mygsonconvert.ServiceErrorException
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException


/**
 *@author wghcwc
 *@date 20-2-4
 */

const val TAG = "Net Error"

suspend fun <T> Call<T>.subscribe(
    scope: CoroutineScope,
    customErrorHandle: (code: Int, e: String?) -> Unit = defaultError
): T? {
    return try {
        withContext( Dispatchers.IO) {
            val result = execute().body()
            if (isActive) {
                result
            } else null
        }
    } catch (e: Exception) {
        if (scope.isActive) {
            Log.e("$TAG URL ", request().url().toString())
            errorHandle(e, customErrorHandle)
        }
        null
    }
}


private fun errorHandle(e: Exception, customErrorHandle: (code: Int, msg: String?) -> Unit) {
    when (e) {
        is ServiceErrorException -> {
            customErrorHandle(e.errorCode, e.message)
        }
        is HttpException -> {
            defaultError(e.code(), e.message())
        }
        is UnknownHostException -> {
            defaultError(400, "无法连接到服务器")
        }
        is SocketTimeoutException -> {
            defaultError(400, "链接超时")
        }
        is ConnectException -> {
            defaultError(500, "链接失败")
        }
        is SocketException -> {
            defaultError(500, "链接关闭")
        }
        is EOFException -> {
            defaultError(500, "链接关闭")
        }
        is IllegalArgumentException -> {
            defaultError(400, "参数错误")
        }
        is SSLException -> {
            defaultError(500, "证书错误")
        }
        is NullPointerException -> {
            defaultError(500, "数据为空")
        }
        else -> {
            defaultError(500, "未知错误")
        }
    }
    Log.e(TAG, e.toString())

}

private val defaultError = fun(_: Int, msg: String?) {
    Log.e(TAG, msg)
}




