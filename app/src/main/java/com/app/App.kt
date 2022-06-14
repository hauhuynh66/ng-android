package com.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.app.broadcast.FirebaseMessageReceiver
import com.google.firebase.messaging.RemoteMessage
import org.opencv.android.OpenCVLoader

class App: Application() {
    private val channel1ID = "CHANNEL_1"
    private val channel2ID = "CHANNEL_2"
    private val channel3ID = "CHANNEL_3"
    private val messageReceive = "MESSAGE_RECEIVED"
    val actionMessageReceived = "ACTION_MESSAGE_RECEIVED"
    companion object {
        val messages:ArrayList<RemoteMessage?> = arrayListOf()
    }

    override fun onCreate() {
        super.onCreate()
        OpenCVLoader.initDebug()
        println("OK")
        createNotificationChannel()
        registerMessageReceiver()
    }

    private fun registerMessageReceiver() {
        val listener:FirebaseMessageReceiver.Listener = object : FirebaseMessageReceiver.Listener{
            override fun onReceive(remoteMessage: RemoteMessage?) {
                messages.add(remoteMessage)
                val intent = Intent(actionMessageReceived)
                sendBroadcast(intent)
            }
        }
        val receiver = FirebaseMessageReceiver(listener)
        registerReceiver(receiver, IntentFilter(messageReceive))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val channel1 = NotificationChannel(
                this.channel1ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel1.description = "This is Channel 1"
            val channel2 = NotificationChannel(
                this.channel2ID,
                "Channel 2",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel2.description = "This is Channel 2"
            val channel3 = NotificationChannel(
                this.channel3ID,
                "Channel 3",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel3.description = "This is Channel 3"
            val notificationManager =
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
            notificationManager.createNotificationChannel(channel3)
        }
    }
}