package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Authentication : ViewModel(){
    val currentState: MutableLiveData<Int> = MutableLiveData(0)
    val firebaseAuth: FirebaseAuth = Firebase.auth
    val username = MutableLiveData("hauhuynh66@gmail.com")
    val password = MutableLiveData("Hauhuynh")

    var isEmergency = false

    fun setState(state : Int){
        currentState.value = state
    }
}