package com.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMessaging : AppCompatActivity() {
    private lateinit var data:ArrayList<RemoteMessage?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fcm)
    }
}