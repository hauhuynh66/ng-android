package com.app.util

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * File Operation
 * Basic Operation on File
 */
class FileUtils {
    companion object{
        /**
         * Create temp file to store image
         */
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

        /**
         * Delete file
         */
        fun deleteFile(path : String) : Boolean {
            val file = File(path)
            return if(!file.exists()){
                false
            }else{
                file.delete()
            }
        }

        /**
         * Move file
         */
        fun moveFile(oldPath : String, newPath : String) : Boolean {
            val file = File(oldPath)
            return if(file.exists()){
                file.copyTo(File(newPath), true)
                file.delete()
                true
            }else{
                false
            }
        }

        /**
         * Delete file (Multiple)
         * Return : number file delete successful
         */
        fun deleteFiles(pathList : List<String>) : Int {
            var count = 0
            pathList.forEach {
                if(deleteFile(it)){
                    count++
                }else{
                    return@forEach
                }
            }
            return count
        }
    }
}