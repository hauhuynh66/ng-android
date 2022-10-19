package com.app.util

import java.text.DecimalFormat
import java.util.*

class Utils {
    companion object{
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