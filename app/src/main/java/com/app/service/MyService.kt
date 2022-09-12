package com.app.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message

class MyService : Service() {
    private var serviceLooper : Looper? = null
    private var serviceHandler : MyServiceHandler? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private inner class MyServiceHandler(lp : Looper) : Handler(lp){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }
}