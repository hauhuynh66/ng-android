package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class LineChart : View{
    data class Data(val y: Number)

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet){
        init()
    }

    private lateinit var axPaint: Paint
    private lateinit var pointPaint: Paint
    private lateinit var linePath : Path
    private lateinit var linePaint : Paint

    private var maxY : Double = 100.0
    private var lineWidth : Float = 5f

    private var padding : Float = 10f

    var data: ArrayList<Data> = arrayListOf(
        Data(10.0),
        Data(20.0),
        Data(80.0),
        Data(20.0),
        Data(50.0),
        Data(11.0),
        Data(30.0),
        Data(44.0),
        Data(76.0),
        Data(120.0)
    )
    private fun init(){
        axPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        axPaint.apply {
            color = Color.BLUE
            strokeWidth = 6f
            style = Paint.Style.STROKE
        }

        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.GREEN
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
        }

        pointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pointPaint.apply {
            color = Color.RED
            strokeWidth = 12f
        }

        linePath = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        data.maxByOrNull {
            it.y.toDouble()
        }?.let {
            maxY = it.y.toDouble()
        }

        var prevX : Float = padding
        var prevY : Float = convertY(data[0].y.toDouble(), maxY)
        linePath.moveTo(prevX, prevY)
        canvas!!.apply {
            canvas.drawLine(padding, padding, padding, height.toFloat() - padding, axPaint)
            canvas.drawLine(padding,height.toFloat()- padding , width.toFloat() - padding, height.toFloat()- padding, axPaint)
        }

        for(pos : Int in 0 until data.size){
            val x = convertX(pos)
            val y = convertY(data[pos].y.toDouble(), maxY)
            canvas.drawPoint(
                x,
                y,
                pointPaint
            )

            val controlX = (prevX + x)/2
            val controlY = max(prevY, y)

            linePath.quadTo(controlX, controlY,x, y)
            canvas.drawPath(linePath, linePaint)
            linePath.moveTo(x, y)
            prevX = x
            prevY = y
        }
    }

    private fun convertX(i : Int) : Float{
        val x = (width-2*padding)/data.size
        return padding + (i+1) * x
    }

    private fun convertY(y : Double, diffY : Double) : Float{
        val ratio = (height-2*padding)/diffY
        return (height - padding - y * ratio).toFloat()
    }

}