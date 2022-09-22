package com.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth : ViewModel() {
    val currentAuth : FirebaseAuth = Firebase.auth
}