package com.app.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object{
        fun getText(cl: Calendar, mode:Int, separator : Char) : String {
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

        fun atEndDate(now : Long) : Long {
            val calendar = Calendar.getInstance()

            calendar.time = Date(now)

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)

            return calendar.timeInMillis
        }

        fun atStartDate(now : Long) : Long {
            val calendar = Calendar.getInstance()

            calendar.time = Date(now)

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.timeInMillis
        }

        fun timeFromLong(duration : Long?) : String {
            val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
            return try {
                formatter.format(duration)
            }catch (e : Exception){
                "00:00"
            }
        }

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