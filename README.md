# kotlin-Net-Demo
kotlin 网络封装demo
```
   launch {
            RetrofitClient.kApi().getCall(MainActivity.baidu).subscribe(this) { code, tmsg->
            //后台自定义异常处理
                e("MainViewModel", msg)
            }?.let {
            //数据正常执行相应逻辑
                d("MainViewModel", it.toString())
            }
```
