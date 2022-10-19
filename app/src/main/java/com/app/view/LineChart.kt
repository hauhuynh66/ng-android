package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import kotlin.math.max

class LineChart : View{
    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet){
        init()
        context!!.theme.obtainStyledAttributes(attributeSet, R.styleable.LineChart, 0, 0).apply {
            try {
                showAxes = getBoolean(R.styleable.LineChart_showAxes, true)
                showPoints = getBoolean(R.styleable.LineChart_showPoints, true)
            }finally {
                recycle()
            }
        }
    }

    private lateinit var axPaint: Paint
    private lateinit var pointPaint: Paint
    private lateinit var linePath : Path
    private lateinit var linePaint : Paint
    private var showAxes = true
    private var showPoints = true

    private var maxY : Double = 100.0
    private var lineWidth : Float = 5f

    private var padding : Float = 10f

    private var data = arrayListOf<Number>(
        10, 20, 80, 30, 60, 40, 120, 30
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
        maxY = data.maxOf {
            it.toDouble()
        }

        var prevX : Float = padding
        var prevY : Float = convertY(data[0].toDouble(), maxY)
        linePath.moveTo(prevX, prevY)
        if(showAxes){
            canvas!!.apply {
                canvas.drawLine(padding, padding, padding, height.toFloat() - padding, axPaint)
                canvas.drawLine(padding,height.toFloat()- padding , width.toFloat() - padding, height.toFloat()- padding, axPaint)
            }
        }
        canvas!!.apply {
            for(pos : Int in 0 until data.size){
                val x = convertX(pos)
                val y = convertY(data[pos].toDouble(), maxY)

                if(showPoints){
                    canvas.drawPoint(
                        x,
                        y,
                        pointPaint
                    )
                }
                val controlX = (prevX + x)/2
                val controlY = max(prevY, y)

                linePath.quadTo(controlX, controlY,x, y)
                canvas.drawPath(linePath, linePaint)
                linePath.moveTo(x, y)
                prevX = x
                prevY = y
            }
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

    fun setData(data : ArrayList<Number>){
        this.data = data
        invalidate()
    }
}