package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import com.app.util.Generator

class BarChart : View {
    private lateinit var linePaint: Paint
    private lateinit var axPaint: Paint
    private lateinit var barPaint : Paint
    private var random : Boolean = false
    private var data = listOf<Number>(
        20,
        10,
        30,
        50,
        40,
        80,
        60,
        90
    )

    private var maxY : Double = 100.0

    private var lineWidth = 40f
    private var barWidth : Float = 200f

    private var distance : Float = 50f

    private var padding : Float = 20f
    private var showAxes : Boolean = true

    private lateinit var linePath : Path
    private lateinit var colorList : List<String>

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0).apply {
            try {
                showAxes = getBoolean(R.styleable.BarChart_showAxes, false)
                random = getBoolean(R.styleable.BarChart_random, false)
            }finally {
                recycle()
            }
        }

        colorList = Generator.generateColor(data.size)

        init()
    }

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

        barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        barPaint.apply {
            color = Color.GREEN
            strokeWidth = barWidth
            style = Paint.Style.STROKE
        }

        linePath = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        maxY = data.maxOf {
            it.toDouble()
        }

        barWidth = 3*((width - 2*padding)/data.size)/5
        distance = ((width - 2*padding)/data.size)/5

        if(showAxes){
            canvas!!.apply {
                canvas.drawLine(padding, padding, padding, height.toFloat() - padding, axPaint)
                canvas.drawLine(padding,height.toFloat()- padding , width.toFloat() - padding, height.toFloat()- padding, axPaint)
            }
        }

        data.forEachIndexed{ i, d ->
            run {
                barPaint.apply {
                    strokeWidth = barWidth
                    color = Color.parseColor(colorList[i])
                }
                val x = convertX(i)
                val y = convertY(d.toDouble(), maxY)
                canvas!!.drawLine(x, height-padding, x , y, barPaint)
            }
        }
    }


    private fun convertX(i : Int) : Float{
        val x = (width - 2*padding)/data.size
        return padding + (i+1)*x - x/2
    }

    private fun convertY(y : Double, diffY : Double) : Float{
        val ratio = (height-2*padding)/diffY
        return (height - padding - y * ratio).toFloat()
    }

    fun setData(list : List<Number>){
        this.data = list
        colorList = Generator.generateColor(data.size)
        invalidate()
    }
}