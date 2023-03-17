package com.general

class NumberUtils {
    companion object{
        fun max(numbers : List<Number>) : Number
        {
            var max = 0f
            numbers.forEach {
                if(it.toFloat() > max)
                {
                    max = it.toFloat()
                }
            }
            return max
        }

        fun min(numbers: List<Number>) : Number
        {
            var min = 0f
            numbers.forEach {
                if(it.toFloat() < min)
                {
                    min = it.toFloat()
                }
            }
            return min
        }

        fun sum(numbers: List<Number>) : Number
        {
            var sum = 0f
            numbers.forEach {
                sum += it.toFloat()
            }
            return sum
        }

        fun average(numbers : List<Number>) : Number
        {
            val sum = sum(numbers)
            return sum.toFloat()/numbers.size.toFloat()
        }
    }
}