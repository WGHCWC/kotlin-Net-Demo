package com.wghcwc.demo

/**
 *@author wghcwc
 *@date 20-1-28
 */

    var name = "张三"
    var nickName: String? = null

    fun main() {
        print(pintName(name))

    }

    fun pintName(str: String): String {
        print("输出姓名:$str")
        return str;
    }

