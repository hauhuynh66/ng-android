package com.app.task

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class QueueTask<T> {
    private val taskQueue : ArrayList<Callable<T>> = arrayListOf()
    private val executor = Executors.newSingleThreadExecutor()
    private val handler: Handler = Handler(Looper.getMainLooper())

    interface Callback<T> {
        fun onTaskComplete(result: T)
        fun onQueueComplete(result: T)
    }
}