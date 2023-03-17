package com.general

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class Permission(val name : List<String>, val context: Context, val callback: Callback? = null) {

    private fun check() : Boolean{
        name.forEach {
            val granted = context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            if(!granted){
                return false
            }
        }
        return true
    }

    private fun request(){
        val activity = (context as AppCompatActivity) ?: return
        val launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionList-> run {
                    name.forEach {
                        val granted = permissionList.getOrDefault(it, false)
                        if(granted){
                            callback?.onGranted()
                        }else{
                            callback?.onDenied(it)
                        }
                    }
                }
            }
        launcher.launch(name.toTypedArray())
    }

    fun checkOrRequest(){
        if(!check()){
            request()
        }
    }

    interface Callback{
        fun onGranted(){

        }

        fun onDenied(permission : String){
            println("Denied : $permission")
        }
    }
}