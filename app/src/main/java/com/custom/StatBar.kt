package com.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.app.ngn.R

class StatBar : View {
    private var bgPaint = Paint()
    private var lgPaint = Paint()
    private var rgPaint = Paint()
    private var txtPaint = Paint()
    private var vLeft : Int = 6
    private var vRight : Int = 7
    var total : Int = vLeft + vRight
    constructor(context: Context?) : super(context){
        bgPaint.apply {
            color = Color.GRAY
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        lgPaint.apply {
            color = Color.YELLOW
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        rgPaint.apply {
            color = Color.BLUE
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val size = 5f
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )
        val customTypeface =
            ResourcesCompat.getCachedFont(context!!, R.font.audiowide)

        txtPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        bgPaint.apply {
            color = Color.GRAY
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        lgPaint.apply {
            color = Color.YELLOW
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        rgPaint.apply {
            color = Color.BLUE
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val size = 5f
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )
        val customTypeface =
            ResourcesCompat.getCachedFont(context!!, R.font.audiowide)

        txtPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val textBounds = Rect()

        txtPaint.getTextBounds(vLeft.toString(),0,vLeft.toString().length, textBounds)
        txtPaint.getTextBounds(vRight.toString(),0,vRight.toString().length, textBounds)
        val h = textBounds.height()
        val b = 100f
        val br = textBounds.width()

        canvas!!.apply {
            val x1 = ((vLeft.toDouble() / total) * (width.toFloat()-2*b)/2).toFloat()
            val x2 = ((vRight.toDouble() / total) * (width.toFloat()-2*b)/2).toFloat()
            drawLine(width.toFloat() - b,height.toFloat()/2, b,height.toFloat()/2, bgPaint)
            drawLine(width.toFloat()/2, height.toFloat()/2, width.toFloat()/2 - x1, height.toFloat()/2, lgPaint)
            drawLine(width.toFloat()/2, height.toFloat()/2, width.toFloat()/2 + x2, height.toFloat()/2, rgPaint)
            drawText(vLeft.toString(), 10f, height.toFloat()/2 + h/2, txtPaint)
            drawText(vRight.toString(),width.toFloat() - br - 15f , height.toFloat()/2 + h/2, txtPaint)
        }
    }

    fun setValue(left : Int, right : Int){
        vLeft = left
        vRight = right
        total = vLeft + vRight
        invalidate()
    }
}