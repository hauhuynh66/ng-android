package com.general

import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

open class Ellipse(val a : Number, val b: Number) {
    fun getX(y : Number) : Number{
        val fx = 1 - (y.toFloat().pow(2) / b.toFloat().pow(2))
        return sqrt(fx * a.toFloat().pow(2))
    }

    fun getY(x : Number) : Number {
        val fy = 1 - (x.toFloat().pow(2) / a.toFloat().pow(2))
        return sqrt(fy * b.toFloat().pow(2))
    }

    fun getArcFromX(x : Number) : Number {
        val y = getY(x)
        return atan((y.toFloat() /x.toFloat()) * (a.toFloat()/b.toFloat()))
    }

    fun getArcFromY(y : Number) : Number {
        val x = getX(y)
        return atan((y.toFloat() /x.toFloat()) * (a.toFloat()/b.toFloat()))
    }
}