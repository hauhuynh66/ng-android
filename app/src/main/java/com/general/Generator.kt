package com.general

import java.util.*

class Generator {
    companion object{
        fun generateString(length:Int):String{
            val sb = StringBuilder()
            val sample = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"
            for (i in 0 until length){
                sb.append(sample[Random().nextInt(sample.length)])
            }
            return sb.toString()
        }

        /**
         * Temporary
         * Change Later
         */
        fun generateColorCode() : String{
            val sb = StringBuilder()
            val sample = "0123456789"
            sb.append("#")
            for(i in 0 until 6){
                sb.append(sample[Random().nextInt(sample.length)])
            }
            return sb.toString()
        }

        /**
         * Temporary
         * Change Later
         */
        fun generateColor(length : Int) : List<String>{
            val list = mutableListOf<String>()
            for(i in 0..length){
                list.add(generateColorCode())
            }
            return list
        }
    }
}