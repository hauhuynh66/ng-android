package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.random.Random

class PieChart : View {
    data class Data(val value : Number)
    private fun sum(a : Data, b : Data) : Data{
        return Data(a.value.toDouble() + b.value.toDouble())
    }
    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint
    private lateinit var outerRectF: RectF
    private lateinit var innerRectF: RectF
    private var lineWidth = 10f
    private var padding = 20f
    private var isDonut = false
    private var donutWidth = 20f
    private var data : ArrayList<Data> = arrayListOf(
        Data(100),
        Data(200),
        Data(500),
        Data(50),
    )
    private lateinit var random : Random

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }

    private fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.GREEN
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
        }

        sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sweepPaint.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }

        val centerX = width/2
        val centerY = height/2
        val r = min(width/2, height/2) - 2*padding
        outerRectF = RectF(centerX - r, centerY - r, centerX + r, centerY + r)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val sum : Double = data.reduce{
            a,b -> sum(a,b)
        }.value.toDouble()
        var currentArc = -90f


        data.forEachIndexed {
            i, it -> run{
                random = Random(i)
                sweepPaint.color = Color.parseColor(randomColor())
                val sweepArc = ((it.value.toDouble()/sum)).toFloat() * 360f
                canvas!!.drawArc(outerRectF, currentArc, sweepArc, true, sweepPaint)
                currentArc += sweepArc
            }
        }
        if(isDonut){
            sweepPaint.color = Color.WHITE
            canvas!!.drawArc(innerRectF, 0f, 360f, true, sweepPaint)
        }
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

}