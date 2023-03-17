package com.general

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

class BitmapUtils {
    companion object {
        fun rotateBitmap(src : Bitmap, angle : Float) : Bitmap{
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, false)
        }

        private fun getBitmap(uri : Uri, contentResolver: ContentResolver) : Bitmap? {
            var dst : Bitmap? = null
            try {
                uri.apply {
                    dst = if(Build.VERSION.SDK_INT < 28){
                        MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            uri
                        )
                    }else {
                        val src = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(src)
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
            return dst
        }

        fun getBitmap(uri: Uri, contentResolver: ContentResolver, format : Bitmap.Config) : Bitmap? {
            val bitmap = getBitmap(uri, contentResolver)
            return bitmap?.copy(format, true)
        }

        fun getBitmapFromUri(uri : Uri, contentResolver: ContentResolver): Bitmap? {
            return if(Build.VERSION.SDK_INT < 28){
                MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    uri
                )
            }else{
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

}