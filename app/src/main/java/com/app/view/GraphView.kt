package com.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.app.data.ForecastData

class GraphView : View{
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet)
            : super(context, attributeSet)

    private lateinit var linePaint: Paint
    private lateinit var pointPaint: Paint
    private lateinit var bounds:Rect
    var data: ForecastData? = null
    private fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.color = Color.BLUE
        linePaint.strokeWidth = 2f
        pointPaint.color = Color.RED
        pointPaint.strokeWidth = 10f
        bounds = Rect()
    }

    override fun onDraw(canvas: Canvas?) {
        init()
        canvas!!.drawLine(5f,5f,5f, height.toFloat(), linePaint)
        canvas.drawLine(5f,height.toFloat(),width.toFloat(), height.toFloat(), linePaint)
        val dY = height/50
        val dX = width/data!!.data.size
        var i = 0
        data!!.apply {
            data.size>0
            for(weather in this.data){
                canvas.drawPoint(5f + i*dX,dY * (50f - weather.temp.toFloat()), pointPaint)
                linePaint.color = Color.BLACK
                linePaint.textSize = 20f
                val s = weather.temp.toString()
                val x = if(i > 0){
                    5f + i*dX - bounds.width()/2
                }else{
                    10f
                }
                val y = dY * (50f - weather.temp.toFloat()) - (bounds.height()*2)
                linePaint.getTextBounds(s ,0, s.length, bounds)
                canvas.drawText(s, x,y,linePaint)
                i++
            }
            linePaint.color = Color.BLUE
            for(j in 0 until i - 1){
                canvas.drawLine(5f + j*dX, dY * (50 - this.data[j].temp.toFloat())
                    ,5f+(j+1)*dX, dY * (50 - this.data[j+1].temp.toFloat()), linePaint)
            }
        }
    }
}