package com.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.app.ngn.R
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ViewUtils {
    companion object{
        fun getWeatherIcon(des: String, context: Context): Drawable {
            when(des){
                else -> {
                    return ContextCompat.getDrawable(context, R.drawable.ic_wb_sunny)!!
                }
            }
        }

        fun formatDate(date:Date):String{
            val sf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            return try {
                sf.format(date)
            }catch (e:Exception){
                ""
            }
        }

        fun parseDate(s:String):Date{
            val sf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            return try {
                sf.parse(s)!!
            }catch (e:Exception){
                Date()
            }
        }

        fun generateString(length:Int):String{
            val sb:StringBuilder = StringBuilder()
            val sample = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890 "
            for (i in 0..length){
                sb.append(sample[Random().nextInt(sample.length)])
            }
            return sb.toString()
        }

        fun getImage(url:String):Bitmap?{
            println(url)
            return try {
                val input = URL(url).openStream()
                return BitmapFactory.decodeStream(input)
            }catch (e:Exception){
                e.printStackTrace()
                null
            }
        }
    }
}