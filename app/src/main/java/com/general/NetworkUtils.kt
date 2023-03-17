package com.general

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils {
    companion object{
        @SuppressLint("MissingPermission")
        fun isNetworkAvailable(context: Context){
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
        }
    }
}