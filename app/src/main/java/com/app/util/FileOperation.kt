package com.app.util

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileOperation {
    companion object{
        fun createImageFile(dir : String) : File?{
            val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            val name = formatter.format(Date()).plus("_IMG.jpg")
            if(!File(dir).exists()){
                val success = File(dir).mkdirs()
                if(!success){
                    return null
                }
            }
            val file = File(dir, name)
            return if(!file.exists()){
                val success = file.createNewFile()
                if(success){
                    file
                }else{
                    null
                }
            }else{
                file
            }
        }
    }
}