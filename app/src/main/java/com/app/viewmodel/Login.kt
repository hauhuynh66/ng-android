package com.app.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : ViewModel(){
    val currentState: MutableLiveData<Int> = MutableLiveData(0)
    val auth: FirebaseAuth = Firebase.auth
    private lateinit var client: SignInClient
    private lateinit var request: BeginSignInRequest

    public fun setState(state : Int){
        currentState.value = state
    }

    fun init(context : Context){
        this.client = Identity.getSignInClient(context)
        this.request = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setServerClientId("529876654956-igc8hqepkbo02v14mr0oc8l7vddkfolp.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    fun googleSignIn(){
        this.client.beginSignIn(this.request)
            .addOnCompleteListener{
                println(it.isSuccessful)
            }
            .addOnFailureListener {
                println(it.message)
            }
    }
}