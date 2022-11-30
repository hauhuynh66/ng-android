package com.app.task

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Callable

/** Download Image Task
 * url : URL to image
 * path : Path to store image
 * return bitmap to downloaded source
 */
class DownloadImageTask(private val url : URL, val path : String? = null) : Callable<Boolean> {
    override fun call(): Boolean {
        val bitmap: Bitmap?
        var success = false
        try {
            val conn = url.openConnection()
            bitmap = BitmapFactory.decodeStream(conn.getInputStream())
            if(path != null){
                val file = File(path)
                if(!file.exists()){
                    file.createNewFile()
                }
                val outputStream = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            success = true
        }catch (e : Exception){
            e.printStackTrace()
        }
        return success
    }
}