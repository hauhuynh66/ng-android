package com.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View

class BatteryReceiver(val view : View) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent?.getIntExtra("level", 0)
    }


}