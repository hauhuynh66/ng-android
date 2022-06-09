package com.app.task

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL
import java.util.concurrent.Callable

class ImageCallable(private val url:String): Callable<Bitmap?> {
    override fun call(): Bitmap? {
        return try {
            val inputStream = URL(url).openStream()
            BitmapFactory.decodeStream(inputStream)
        }catch (e:Exception){
            null
        }
    }
}