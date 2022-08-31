package com.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.random.Random

class BarChart : View {
    data class Data(val value : Number)
    private lateinit var linePaint: Paint
    private lateinit var axPaint: Paint
    private lateinit var barPaint : Paint
    private var data : List<Data> = arrayListOf(
        Data(80.0),
        Data(100.0),
        Data(50.0),
        Data(40.0),
        Data(90.0),
        Data(50.0),
        Data(70.0),
        Data(20.0),
        Data(80.0),
        Data(80.0)
    )

    private var maxY : Double = 100.0

    private var lineWidth = 40f
    private var barWidth : Float = 200f

    private var distance : Float = 50f

    private var padding : Float = 20f

    private lateinit var linePath : Path
    private lateinit var random : Random

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
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

        data.maxByOrNull {
            it.value.toDouble()
        }?.let {
            maxY = it.value.toDouble()
        }

        barWidth = 3*((width - 2*padding)/data.size)/5
        distance = ((width - 2*padding)/data.size)/5

        canvas!!.apply {
            canvas.drawLine(padding, padding, padding, height.toFloat() - padding, axPaint)
            canvas.drawLine(padding,height.toFloat()- padding , width.toFloat() - padding, height.toFloat()- padding, axPaint)
        }

        data.forEachIndexed{ i, d ->
            run {
                random = Random(i)
                barPaint.apply {
                    strokeWidth = barWidth
                    color = Color.parseColor(randomColor())
                }
                val x = convertX(i)
                val y = convertY(d.value.toDouble(), maxY)
                canvas.drawLine(x, height-padding, x , y, barPaint)
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

    fun setData(data: ArrayList<Data>){
        this.data = data
        invalidate()
    }
}