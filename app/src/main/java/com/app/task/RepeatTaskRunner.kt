package com.app.task

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class RepeatTaskRunner(val period : Int, val times : Int) {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    interface Callback<T>{
        fun onUpdate(result:T)
        fun onComplete(result:T)
    }

    fun <T> schedule(call: Callable<T>, callback: TaskRunner.Callback<T>){

    }
}