package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarChart : View {
    data class Data(val value : Number)
    private lateinit var linePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var axPaint: Paint
    private lateinit var data : List<Data>

    private var maxY : Double = 100.0
    private var minY : Double = 0.0

    private var barWidth : Float = 20f

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }


    private fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {

        }

        axPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }



}