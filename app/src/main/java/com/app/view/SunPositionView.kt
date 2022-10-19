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
    private var sunset: Long = 0
    private var sunrise: Long = 0
    private var current: Long = 0

    private var dashColor : Int = Color.BLACK
    private var lineColor : Int = Color.YELLOW

    private var cx : Float = 0f
    private var cy : Float = 0f
    private var arcRect : RectF = RectF()
    private val bounds : Rect = Rect()

    private var dashedLinePaint : Paint = Paint()
    private var linePaint : Paint = Paint()
    private var textPaint : Paint = Paint()
    private var srText = ""
    private var ssText = ""
    private val padding = 20f

    constructor(context: Context?) : super(context){
        dashedLinePaint.apply {
            color = Color.BLUE
            strokeWidth = 6f
            style = Paint.Style.STROKE
            isAntiAlias = true
            pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
        }

        linePaint.apply {
            color = Color.YELLOW
            strokeWidth = 10f
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

        textPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        dashedLinePaint.apply {
            color = Color.BLUE
            strokeWidth = 6f
            style = Paint.Style.STROKE
            isAntiAlias = true
            pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
        }

        linePaint.apply {
            color = Color.YELLOW
            strokeWidth = 10f
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

        textPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        arcRect = RectF(padding, padding, width.toFloat() - padding, (height.toFloat() - padding)*2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val dis: Long = sunset - sunrise
        val c: Long = current - sunrise

        canvas!!.drawArc(arcRect, 180f, 180f, false, dashedLinePaint)

        if(c > 0){
            val deg = (c.toDouble()/dis.toDouble()) * 180.0
            canvas.drawArc(arcRect, 180f, deg.toFloat(), false, linePaint)
        }else{

        }

        textPaint.getTextBounds(srText, 0, srText.length, bounds)
        canvas.drawText(srText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)

        textPaint.getTextBounds(ssText, 0, ssText.length, bounds)
        canvas.drawText(ssText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
    }

    fun display(sunrise : Long, sunset : Long){
        this.sunrise = sunrise
        this.sunset = sunset
        this.srText = format(Date(this.sunrise*1000))
        this.ssText = format(Date(this.sunset*1000))

        this.current = System.currentTimeMillis()/1000

        invalidate()
    }

    private fun format(date : Date) : String{
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }
}