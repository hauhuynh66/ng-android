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
        val pX = 5f
        val pY = 10f
        val dY = height/50
        val dX = width/data!!.data.size
        var i = 0
        canvas!!.apply {
            canvas.drawLine(pX,pY,pX, height.toFloat() - pY, linePaint)
            canvas.drawLine(pX,height.toFloat()- pY,width.toFloat(), height.toFloat()- pY, linePaint)
        }
        data!!.apply {
            data.size>0
            for(weather in this.data){
                canvas.drawPoint(pX + i*dX,dY * (50f - weather.temp.toFloat()) - pY, pointPaint)
                linePaint.color = Color.BLACK
                linePaint.textSize = 20f
                val s = weather.temp.toString()
                val x = if(i > 0){
                    pX + i*dX - bounds.width()/2
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
                canvas.drawLine(pX + j*dX, dY * (50 - this.data[j].temp.toFloat()) - pY
                    ,pX+(j+1)*dX, dY * (50 - this.data[j+1].temp.toFloat()) - pY, linePaint)
            }
        }
    }
}