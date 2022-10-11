package com.app.task

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class TaskRunner {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler:Handler = Handler(Looper.getMainLooper())

    interface Callback<T>{
        fun onComplete(result:T)
    }

    fun <T> execute(call:Callable<T>, callback: Callback<T>){
        executor.execute{
            val result = call.call()
            handler.post {
                callback.onComplete(result)
            }
        }
    }
}