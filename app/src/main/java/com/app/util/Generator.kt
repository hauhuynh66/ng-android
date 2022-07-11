package com.app.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.app.ngn.R
import java.util.*

class Generator {
    companion object{
        fun generateString(length:Int):String{
            val sb:StringBuilder = StringBuilder()
            val sample = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"
            for (i in 0..length){
                sb.append(sample[Random().nextInt(sample.length)])
            }
            return sb.toString()
        }

        fun getWeatherIcon(des: String, context: Context): Drawable {
            when{
                else -> {
                    return ContextCompat.getDrawable(context, R.drawable.ic_wb_sunny)!!
                }
            }
        }
    }
}