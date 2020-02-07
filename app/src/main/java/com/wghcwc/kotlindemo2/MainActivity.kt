package com.wghcwc.kotlindemo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.wghcwc.kotlindemo2.net.RetrofitClient
import com.wghcwc.kotlindemo2.net.observer.KBaseObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    companion object {
        val baidu = "https://www.baidu.com"
        val baidu2 = "https://www.youtube.com"
    }

    val viewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("sss", "start ")
        val a = viewModel.getTest()
//        viewModel.getsu2()
//        viewModel.cancel()
//       val b= viewModel.getsu3()


        Log.d("sss", "end ")

        hello.setOnClickListener { viewModel.cancel() }
    }

    /* fun hhh2():ResponseBody{
         return  GlobalScope.async {
             val to:ResponseBody
             RetrofitClient.api().get("https://www.baidu.com")
                 .subscribe(KBaseObserver{ t->
                    to=t

                 })
             return@async t
         }.await()*/


    fun hhh() {
        RetrofitClient.api().get(baidu)
            .subscribe(KBaseObserver { t ->
                hello.text = t.toString()
            })
    }

    fun hhh3() {

        println()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}



