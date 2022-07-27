package com.app.view

import android.content.Context
import android.graphics.*
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
    private var hourHandPaint : Paint = Paint()
    private var minHandPaint : Paint = Paint()
    private var secHandPaint : Paint = Paint()
    val padding = 10f

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

        hourHandPaint.apply {
            color = Color.BLUE
            strokeWidth = 20f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        minHandPaint.apply {
            color = Color.YELLOW
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        secHandPaint.apply {
            color = Color.RED
            strokeWidth = 5f
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

        hourHandPaint.apply {
            color = Color.BLUE
            strokeWidth = 20f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        minHandPaint.apply {
            color = Color.YELLOW
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        secHandPaint.apply {
            color = Color.RED
            strokeWidth = 5f
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
            drawHands(canvas, System.currentTimeMillis(), Point(cx.toInt(), cy.toInt()))
            postInvalidateDelayed(1000)

        }
    }

    private fun drawHands(canvas: Canvas?, c : Long, center : Point){
        canvas!!.apply {
            val r = min(width, height)/2 - padding
            val hDistance = r/2
            val mDistance = 2*r/3
            val sDistance = 3*r/4
            val date = Date(c)
            val p1 = -(date.hours/12.0)*360f - (date.minutes/60.0)*30f - 180f
            val p2 = -(date.minutes/60.0)*360f - (date.seconds/60.0)*6f - 180f
            val p3 = -(date.seconds/60.0)*360f - 180f
            val hDeg = ((p1 * Math.PI)/180f).toFloat()
            val mDeg = ((p2 * Math.PI)/180f).toFloat()
            val sDeg = ((p3 * Math.PI)/180f).toFloat()

            val x1 = center.x + sin(hDeg)*(hDistance)
            val y1 = center.y + cos(hDeg)*(hDistance)
            val x2 = center.x + sin(mDeg)*(mDistance)
            val y2 = center.y + cos(mDeg)*(mDistance)
            val x3 = center.x + sin(sDeg)*(sDistance)
            val y3 = center.y + cos(sDeg)*(sDistance)
            drawLine(center.x.toFloat(), center.y.toFloat(), x1, y1, hourHandPaint)
            drawLine(center.x.toFloat(), center.y.toFloat(), x2, y2, minHandPaint)
            drawLine(center.x.toFloat(), center.y.toFloat(), x3, y3, secHandPaint)
        }
    }
}