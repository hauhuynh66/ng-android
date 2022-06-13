package com.app.service

import android.content.Intent
import com.app.data.FCMData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService(): FirebaseMessagingService() {
    private val filter = "MESSAGE_RECEIVED"
    private var data:ArrayList<FCMData> = arrayListOf()
    constructor(data:ArrayList<FCMData>): this(){
        this.data = data
    }
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.data.isNotEmpty()){
            data.add(FCMData(message.data.toString(), message.from!!))
            val i = Intent()
            i.putExtra("news", message)
            val intent = Intent(this.filter)
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