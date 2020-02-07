package com.wghcwc.kotlindemo2

import com.wghcwc.mygsonconvert.ServiceErrorException
import kotlinx.coroutines.*
import retrofit2.Call
import java.io.EOFException
import java.lang.NullPointerException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @author wghcwc
 * @date 20-2-2
 */
class KObservable<T>(private var deferred: Deferred<T>) {

    suspend fun subscribe(customErrorHandle: (code: Int, e: String?) -> Unit = defaultError): T? {
        return try {
            deferred.await()
        } catch (e: Exception) {
            errorHandle(e, customErrorHandle)
            null
        }
    }

    private fun errorHandle(e: Exception, coustum: (code: Int, msg: String?) -> Unit) {
        when (e) {
            is ServiceErrorException -> {
                coustum(e.errorCode, e.message)
            }
            is UnknownHostException -> {
                onError(400, "无法连接到服务器")
            }
            is SocketTimeoutException -> {
                onError(400, "链接超时")
            }
            is SocketException -> {
                onError(500, "链接关闭")
            }
            is EOFException -> {
                onError(500, "链接关闭")
            }
            is IllegalArgumentException -> {
                onError(400, "参数错误")
            }
            is SSLException -> {
                onError(500, "证书错误")
            }
            is NullPointerException -> {
                onError(500, "数据为空")
            }
            else -> {
                onError(500, "未知错误")
            }
        }

    }

    private var defaultError = fun(code: Int, msg: String?) {

    }

    private fun onError(code: Int, s: String) {
        println("$s++++++++++++++")
    }


}