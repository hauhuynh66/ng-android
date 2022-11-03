package com.app.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface

class BitmapUtils {
    companion object {
        fun rotateBitmap(src : Bitmap, angle : Float) : Bitmap{
            val matrix : Matrix = Matrix()
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

        fun rotateImage(path : String) : Bitmap {
            val exif = ExifInterface(path)
            val bitmap = BitmapFactory.decodeFile(path)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            return when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90->{
                    rotateBitmap(bitmap, 90f)
                }
                ExifInterface.ORIENTATION_ROTATE_180->{
                    rotateBitmap(bitmap, 180f)
                }
                ExifInterface.ORIENTATION_ROTATE_270->{
                    rotateBitmap(bitmap, 270f)
                }
                else->{
                    bitmap
                }
            }
        }
    }

}