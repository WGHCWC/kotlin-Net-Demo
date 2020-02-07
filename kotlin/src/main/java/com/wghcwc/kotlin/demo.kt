package com.wghcwc.kotlin



/**
 *@author wghcwc
 *@date 20-2-1
 */

var nickName: String? = null

fun main() {
    var age = 18
    var name = "张三"
//    print(pintName(name))
//    resClass(TestB::class)
//    printIn(TestA.`in`)
//    val a:String? = TestA.getString("")
//    print(a?.length)
//    Test.printf("ss")
//    printIn("dd %d,%d")

//    println("")

//    fun say(time:Int=10){
//        if(time>0){
//
//            println(time)
//            say(time-1)
//        }
//    }
//    say()
//    val run = Runnable { println("ss") }
//    val me: () -> Unit
//    me = run::run
//    print3(me)

    print3(1)

}

fun print3(a: Int) {
    var a = 2
    if (a > 0) {
       val a = 3

    }
    print(a)

}

fun inc(num: Int) {
    val num = 2
    if (num > 0) {
        val num = 3
    }
    println("num: " + num)
}

fun pintName(str: String): String {
    print("输出姓名:$str")
    return str
}

fun resClass(clz: Class<TestA>) {
    print(clz.simpleName)
}


fun printIn(str: String) {
    print(str)
}

fun lambda1(testA: Runnable) {
    testA.run()
}

fun print3(meth: () -> Unit) {
    meth()

}

object Test {
    @JvmStatic
    fun printf(str: String = "ddd") = print(str)
}

