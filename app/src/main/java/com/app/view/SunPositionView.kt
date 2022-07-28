package com.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.app.ngn.R
import java.text.SimpleDateFormat
import java.util.*

class SunPositionView : View {
    var sunset: Long = 0
    var sunrise: Long = 0
    var current: Long = 0

    private var dashedLinePaint : Paint = Paint()
    private var linePaint : Paint = Paint()
    private var textPaint : Paint = Paint()
    private val padding = 20f

    constructor(context: Context?) : super(context){
        dashedLinePaint.color = Color.BLUE
        dashedLinePaint.strokeWidth = 6f
        dashedLinePaint.style = Paint.Style.STROKE
        dashedLinePaint.isAntiAlias = true
        dashedLinePaint.pathEffect = DashPathEffect(floatArrayOf(30f, 10f), 0f)

        linePaint.color = Color.YELLOW
        linePaint.strokeWidth = 10f
        linePaint.style = Paint.Style.STROKE
        linePaint.isAntiAlias = true

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
        dashedLinePaint.color = Color.BLUE
        dashedLinePaint.strokeWidth = 6f
        dashedLinePaint.style = Paint.Style.STROKE
        dashedLinePaint.isAntiAlias = true
        dashedLinePaint.pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)

        linePaint.color = Color.YELLOW
        linePaint.strokeWidth = 10f
        linePaint.style = Paint.Style.STROKE
        linePaint.isAntiAlias = true

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

    fun reload(current : Long){
        this.current = current
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rectF = RectF(padding, padding, width.toFloat() - padding, (height.toFloat() - padding)*2)
        canvas!!.drawArc(rectF, 180f, 180f, false, dashedLinePaint)

        val dis: Long = sunset - sunrise
        val c: Long = current - sunrise
        if(c > 0){
            val deg = (c.toDouble()/dis.toDouble()) * 180.0
            canvas.drawArc(rectF, 180f, deg.toFloat(), false, linePaint)
        }

        val bounds = Rect()

        val s1 = format(Date(sunrise*1000))
        textPaint.getTextBounds(s1, 0, s1.length, bounds)
        canvas.drawText(s1, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)

        val s2 = format(Date(sunset*1000))
        textPaint.getTextBounds(s2, 0, s2.length, bounds)
        canvas.drawText(s2, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
    }


    private fun format(date : Date) : String{
        val sdf = SimpleDateFormat("hh:mm a")
        return sdf.format(date)
    }
}