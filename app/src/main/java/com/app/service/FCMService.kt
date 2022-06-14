package com.app.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    private val filter = "MESSAGE_RECEIVED"
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val i = Intent()
        i.putExtra("news", message)
        if (message.data.isNotEmpty()) {
            val intent = Intent(filter)
            intent.putExtra("news", message)
            sendBroadcast(intent)
        }
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


}