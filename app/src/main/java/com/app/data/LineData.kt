package com.app.data

import android.graphics.Color

data class LineData(
    val name : String,
    val value : Any? = null,
    val icon : Int? = null,
    val option : LineDisplayOption = LineDisplayOption())

enum class LineStyle{
    Style1,
    Style2
}

class LineDisplayOption(val color : Int = Color.BLACK, val textSize : Float = 6f, val gravity: Int? = null)