package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

class LineChart : View{
    data class Data(val x : Int, val y: Double)

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
    private var minY : Double = 0.0
    private var lineWidth : Float = 5f

    private var padding : Float = 10f

    var data: ArrayList<Data> = arrayListOf(
        Data(1, 10.0),
        Data(2, 20.0),
        Data(3, 80.0),
        Data(4, 20.0),
        Data(5, 50.0),
        Data(6, 11.0),
        Data(7, 30.0),
        Data(8, 44.0),
        Data(9, 76.0),
        Data(10, 120.0)
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
            it.y
        }?.let {
            maxY = it.y
        }

        data.minByOrNull {
            it.y
        }?.let {
            minY = it.y
        }

        println(maxY)
        println(minY)

        var prevX : Float = padding
        var prevY : Float = convertY(data[0].y, maxY - minY)
        linePath.moveTo(padding, convertY(data[0].y, maxY - minY))
        canvas!!.apply {
            canvas.drawLine(padding, padding, padding, height.toFloat() - padding, axPaint)
            canvas.drawLine(padding,height.toFloat()- padding , width.toFloat() - padding, height.toFloat()- padding, axPaint)
        }

        for(pos : Int in 0 until data.size){
            val x = convertX(data[pos].x)
            val y = convertY(data[pos].y, maxY - minY)
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

    private fun convertX(x : Int) : Float{
        val ratio = (width-2*padding)/data.size
        return (x * ratio)
    }

    private fun convertY(y : Double, diffY : Double) : Float{
        val ratio = (height-2*padding)/diffY
        return (height + padding - y * ratio).toFloat()
    }

}