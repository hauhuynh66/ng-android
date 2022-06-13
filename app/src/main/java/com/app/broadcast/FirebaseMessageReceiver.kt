package com.app.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageReceiver(private val listener: Listener) : BroadcastReceiver() {
    interface Listener {
        fun onReceive(remoteMessage: RemoteMessage?)
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val message: RemoteMessage? = intent.getParcelableExtra("new")
        listener.onReceive(message)
    }
}