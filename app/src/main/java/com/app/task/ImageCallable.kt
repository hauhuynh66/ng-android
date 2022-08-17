package com.app.task

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Callable

class ImageCallable(private val url : String) : Callable<Bitmap?> {
    override fun call(): Bitmap? {
        return try{
            val iStream : InputStream = URL(this.url).content as InputStream
            BitmapFactory.decodeStream(iStream)
        }catch (e : Exception){
            null
        }

    }
}