package com.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import org.opencv.android.OpenCVLoader

class App: Application() {
    private val messageReceive = "MESSAGE_RECEIVED"
    val actionMessageReceived = "ACTION_MESSAGE_RECEIVED"

    companion object {
        const val notificationChannel1 = "CHANNEL_1"
    }

    override fun onCreate() {
        super.onCreate()
        OpenCVLoader.initDebug()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val channel1 = NotificationChannel(
                notificationChannel1,
                "Channel 1",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel1.description = "This is Channel 1"

            val notificationManager =
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            notificationManager.createNotificationChannel(channel1)
        }
    }
}