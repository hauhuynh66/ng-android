package com.app.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class Check {
    companion object{
        fun checkPermissions(context: Context, permissions: ArrayList<String>):Boolean{
            for (permission in permissions){
                context.apply {
                    if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                        return false
                    }
                }
            }
            return true
        }
    }
}