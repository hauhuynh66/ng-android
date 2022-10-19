package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import kotlin.random.Random

class BarChart : View {
    data class Data(val value : Number)
    private lateinit var linePaint: Paint
    private lateinit var axPaint: Paint
    private lateinit var barPaint : Paint
    private var data = arrayListOf<Number>(
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
    private lateinit var random : Random

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0).apply {
            try {
                showAxes = getBoolean(R.styleable.BarChart_showAxes, false)
            }finally {
                recycle()
            }
        }
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
                random = Random(i)
                barPaint.apply {
                    strokeWidth = barWidth
                    color = Color.parseColor(randomColor())
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

    private fun randomColor() : String{
        val chars = "0123456789"
        val ret = StringBuilder()
        ret.append("#")
        for(i in 0 until 6){
            ret.append(chars[random.nextInt(chars.length-1)])
        }
        return ret.toString()
    }

    fun setData(data : ArrayList<Number>){
        this.data = data
        invalidate()
    }
}