package com.app.util

import java.text.SimpleDateFormat
import java.util.*

class Formatter {
    companion object{
        fun formatDate(date: Date, pattern : String) : String{
            val sf = SimpleDateFormat(pattern, Locale.getDefault())
            return try {
                sf.format(date)
            }catch (e:Exception){
                ""
            }
        }

        fun parseDate(s:String, pattern: String): Date {
            val sf = SimpleDateFormat(pattern, Locale.getDefault())
            return try {
                sf.parse(s)!!
            }catch (e:Exception){
                Date()
            }
        }
    }
}