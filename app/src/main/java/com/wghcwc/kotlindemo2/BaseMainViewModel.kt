package com.wghcwc.kotlindemo2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

/**
 *@author wghcwc
 *@date 20-2-4
 */
open class BaseMainViewModel:ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

}