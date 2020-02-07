package com.wghcwc.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce

/**
 *@author wghcwc
 *@date 20-2-1
 */
fun main(args: Array<String>) = runBlocking<Unit> {
    /*  val job=launch {
          repeat(100){
              println("in job wait")
              delay(500)
          }
      }
      println("main wait start")
      delay(1500)
      println("main wait end")
      job.cancel()
      println("job cancel,finish")
      val job2=async {

          println("in job2")
          return@async "hhhh"
      }

      println("in${job2.await()}")

  */

/*    GlobalScope.launch {
        println("协程 开始执行，时间:  ${System.currentTimeMillis()}")
        val token = getToken()
        val response = getResponse(token)
        setText(response)
    }

    delay(2000)*/

 /*   val job=  launch {
        delay(1)
        println("+++++++++++++++++")
    }*/
//    delay(2)
//    println("job join")
//    job.join()
   /* repeat(1000000){
        println("job join")

    }*/
    val job = launch {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并且等待它结束
    println("main: Now I can quit.")

}

fun main2() = runBlocking {
    launch {
        println("Task from runBlocking")

        delay(2000L)
        println("Task from runBlocking")
    }

    coroutineScope {
        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope")
    }

    println("Coroutine scope is over")
}

suspend fun getToken(): String {
    println("getToken 开始执行，时间:  ${System.currentTimeMillis()}")
    delay(300)
    println("getToken 结束，时间:  ${System.currentTimeMillis()}")

    return "ask"
}


suspend fun getResponse(token: String): String {
    println("getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
    delay(100)
    println("getResponse 开始结束，时间:  ${System.currentTimeMillis()}")
    return "response"
}

fun setText(response: String) {
    println("setText 执行，时间:  ${System.currentTimeMillis()}")
}
fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) send(x++) // 在流中开始从 1 生产无穷多个整数
}
