package com.custom

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
    private var calendar : Calendar = Calendar.getInstance()
    private var linePaint : Paint = Paint()
    private var textPaint : Paint = Paint()
    private var pointPaint : Paint = Paint()
    private var hourHandPaint : Paint = Paint()
    private var minHandPaint : Paint = Paint()
    private var secHandPaint : Paint = Paint()
    private val bounds : Rect = Rect()
    private var clockRect : RectF = RectF()
    private var r : Float = 0f
    private var cx : Float = 0f
    private var cy : Float = 0f

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        r = (min(width, height).toFloat()/2) - padding
        cx = width.toFloat()/2
        cy = height.toFloat()/2

        clockRect = RectF(cx - r, cy - r, cx + r, cy + r)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.apply {
            drawPoint(width.toFloat()/2, height.toFloat()/2, pointPaint)
            drawArc(clockRect, 0f, 360f, false, linePaint)

            for(i in 0 until 12){
                val arcDeg = -i * 30f - 180f
                val arc = arcDeg * (Math.PI/180)
                val distance = 20f
                val x1 = cx + sin(arc)*(r)
                val y1 = cy + cos(arc)*(r)
                val x2 = cx + sin(arc)*(r-distance)
                val y2 = cy + cos(arc)*(r-distance)
                drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), linePaint)
                textPaint.getTextBounds(i.toString(), 0, i.toString().length, bounds)
            }
            drawHands(canvas, System.currentTimeMillis(), cx, cy)
            postInvalidateDelayed(1000)

        }
    }

    private fun drawHands(canvas: Canvas, c : Long, cx : Float, cy : Float){
        canvas.apply {
            val r = min(width, height)/2 - padding
            val hDistance = r/2
            val mDistance = 2*r/3
            val sDistance = 3*r/4
            calendar.time = Date(c)
            val p1 = -(calendar.get(Calendar.HOUR)/12.0)*360f - (calendar.get(Calendar.MINUTE)/60.0)*30f - 180f
            val p2 = -(calendar.get(Calendar.MINUTE)/60.0)*360f - (calendar.get(Calendar.SECOND)/60.0)*6f - 180f
            val p3 = -(calendar.get(Calendar.SECOND)/60.0)*360f - 180f
            val hDeg = ((p1 * Math.PI)/180f).toFloat()
            val mDeg = ((p2 * Math.PI)/180f).toFloat()
            val sDeg = ((p3 * Math.PI)/180f).toFloat()

            val x1 = cx + sin(hDeg)*(hDistance)
            val y1 = cy + cos(hDeg)*(hDistance)
            val x2 = cx + sin(mDeg)*(mDistance)
            val y2 = cy + cos(mDeg)*(mDistance)
            val x3 = cx + sin(sDeg)*(sDistance)
            val y3 = cy + cos(sDeg)*(sDistance)
            drawLine(cx, cy, x1, y1, hourHandPaint)
            drawLine(cx, cy, x2, y2, minHandPaint)
            drawLine(cx, cy, x3, y3, secHandPaint)
        }
    }
}