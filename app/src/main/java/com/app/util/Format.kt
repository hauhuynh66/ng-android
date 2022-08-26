package com.app.util

import java.text.SimpleDateFormat
import java.util.*

class Format {
    companion object{
        fun formatDate(date: Date) : String{
            val sf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            return try {
                sf.format(date)
            }catch (e:Exception){
                ""
            }
        }

        fun parseDate(s:String): Date {
            val sf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            return try {
                sf.parse(s)!!
            }catch (e:Exception){
                Date()
            }
        }

        fun formatDateV3(date : Date) : String{
            val sf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return try {
                sf.format(date)
            }catch (e:Exception){
                ""
            }
        }
    }
}