package com.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.data.chart.TwoValueData
import kotlin.math.min

class PolarAreaChart : View {
    private var data : ArrayList<TwoValueData> =
        arrayListOf(
            TwoValueData(1, 10),
            TwoValueData(5, 20),
            TwoValueData(8, 60),
            TwoValueData(2, 30),
            TwoValueData(6, 50),
        )
    private fun sum(d1 : TwoValueData, d2 : TwoValueData) : TwoValueData{
        return TwoValueData(d1.x.toDouble() + d2.x.toDouble(), d1.y.toDouble() + d2.y.toDouble())
    }

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
        val totalY = data.reduce{
            a, b -> sum(a, b)
        }.y

        val r = min(width, height)/2f
        cx = width/2f
        cy = height/2f

        var currentArc = 0f

        canvas!!.apply {
            data.forEach {
                val arc = (totalY.toFloat()/360f) * it.y.toFloat()

                currentArc += arc
            }
        }
    }

    private fun drawData(start : Float,arc : Float, r: Float, canvas: Canvas){
        val rect = RectF(cx - r, cy - r, cx + r, cy + r)
        canvas.drawArc(rect, start, arc, false, sweepPaint)
    }
}