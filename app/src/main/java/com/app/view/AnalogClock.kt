package com.app.view

import android.content.Context
import android.graphics.*
import android.icu.util.Calendar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.app.ngn.R
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class AnalogClock : View {
    private var linePaint : Paint = Paint()
    private var textPaint : Paint = Paint()
    private var pointPaint : Paint = Paint()

    constructor(context: Context?) : super(context){
        linePaint.apply {
            color = Color.BLACK
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        pointPaint.apply {
            color = Color.BLACK
            strokeWidth = 30f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val size = 5f
        textPaint.color = Color.BLACK
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )
        textPaint.textSize = textSize
        val customTypeface =
            ResourcesCompat.getCachedFont(context!!, R.font.audiowide)
        textPaint.typeface = customTypeface
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        linePaint.apply {
            color = Color.BLACK
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        pointPaint.apply {
            color = Color.BLACK
            strokeWidth = 30f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val size = 5f
        textPaint.color = Color.BLACK
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )
        textPaint.textSize = textSize
        val customTypeface =
            ResourcesCompat.getCachedFont(context!!, R.font.audiowide)
        textPaint.typeface = customTypeface
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val padding = 10f
        canvas!!.apply {
            val r = (min(width, height).toFloat()/2) - padding
            val cx = width.toFloat()/2
            val cy = height.toFloat()/2
            val clockRectF = RectF(cx - r, cy - r, cx + r, cy + r)
            drawPoint(width.toFloat()/2, height.toFloat()/2, pointPaint)
            drawArc(clockRectF, 0f, 360f, false, linePaint)
            for(i in 0 until 12){
                val arcDeg = -i * 30f - 180f
                val arc = arcDeg * (Math.PI/180)
                val bounds = Rect()
                val distance = 20f
                val x1 = cx + sin(arc)*(r)
                val y1 = cy + cos(arc)*(r)
                val x2 = cx + sin(arc)*(r-distance)
                val y2 = cy + cos(arc)*(r-distance)
                drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), linePaint)
                textPaint.getTextBounds(i.toString(), 0, i.toString().length, bounds)
            }
        }
    }
}