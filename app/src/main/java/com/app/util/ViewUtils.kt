package com.app.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.app.ngn.R
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
    }
}