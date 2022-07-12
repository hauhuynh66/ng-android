package com.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppInit : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser==null){
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }else{
            val appIntent = Intent(this, Navigator::class.java)
            startActivity(appIntent)
        }
    }
}