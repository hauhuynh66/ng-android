package com.app.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.max

class Utils {
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

        class ZoomOutPageTransformer: ViewPager2.PageTransformer{
            private val minScale = 0.85f
            private val minAlpha = 0.5f
            override fun transformPage(page: View, position: Float) {
                page.apply {
                    val pageWidth = width
                    val pageHeight = height
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 1 -> {

                            val scaleFactor = max(minScale, 1 - abs(position))
                            val vertMargin = pageHeight * (1 - scaleFactor) / 2
                            val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
                            translationX = if (position < 0) {
                                horizontalMargin - vertMargin / 2
                            } else {
                                horizontalMargin + vertMargin / 2
                            }

                            scaleX = scaleFactor
                            scaleY = scaleFactor

                            alpha = (minAlpha +
                                    (((scaleFactor - minScale) / (1 - minScale)) * (1 - minAlpha)))
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }

        class DepthPageTransFormer : ViewPager.PageTransformer{
            private val minScale = 0.75f
            override fun transformPage(page: View, position: Float) {
                page.apply {
                    val pageWidth = width
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 0 -> {
                            alpha = 1f
                            translationX = 0f
                            scaleX = 1f
                            scaleY = 1f
                        }
                        position <= 1 -> {
                            alpha = 1 - position

                            translationX = pageWidth * -position

                            val scaleFactor = (minScale + (1 - minScale) * (1 - Math.abs(position)))
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }

        fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
        }

        fun getText(cl:Calendar, mode:Int, separator : Char) : String {
            val sb:StringBuilder = StringBuilder()
            val f = DecimalFormat("00")
            when(mode){
                1->{
                    sb.append(cl.get(Calendar.YEAR))
                    sb.append(separator.toString())
                    sb.append(f.format(cl.get(Calendar.MONTH)+1))
                    sb.append(separator.toString())
                    sb.append(f.format(cl.get(Calendar.DATE)))
                }
                2->{
                    sb.append(f.format(cl.get(Calendar.HOUR)))
                    sb.append(":")
                    sb.append(f.format(cl.get(Calendar.MINUTE)))
                }

            }
            return sb.toString()
        }
    }
}