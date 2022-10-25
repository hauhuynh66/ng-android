package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PolarAreaChart : View {
    private var data : ArrayList<Pair<Number, Number>> = arrayListOf()

    private var cx: Float = 0f
    private var cy: Float = 0f

    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint
    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }

    fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.BLACK
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

        sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sweepPaint.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun drawData(start : Float,arc : Float, r: Float, canvas: Canvas){
        val rect = RectF(cx - r, cy - r, cx + r, cy + r)
        canvas.drawArc(rect, start, arc, false, sweepPaint)
    }
}