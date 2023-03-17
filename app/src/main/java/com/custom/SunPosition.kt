package com.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.app.data.weather.SunState
import com.app.ngn.R
import com.general.DateTimeUtils
import com.general.DateTimeUtils.Companion.formatDate
import com.general.Ellipse
import java.util.*
import kotlin.math.abs

class SunPosition : View {
    private var sunset: Long = 0
    private var sunrise: Long = 0
    private var current: Long = 0

    private var dashColor : Int = Color.BLACK
    private var lineColor : Int = Color.YELLOW
    private var bitmapSize : Float = 60f

    private var arcRect : RectF = RectF()
    private var outerRect : RectF = RectF()
    private var ellipse : Ellipse = Ellipse(0,0)
    private val bounds : Rect = Rect()

    private var dashedLinePaint : Paint = Paint()
    private var linePaint : Paint = Paint()
    private var textPaint : Paint = Paint()
    private var offset : Float = 10f
    private val padding = 20f

    private fun getState(sunrise: Long, sunset: Long, current: Long) : SunState {
        return if(current > sunset){
            SunState.State2
        }else if(current < sunrise){
            SunState.State3
        }else{
            SunState.State1
        }
    }

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.SunPosition, 0, 0).apply {
            try {
                dashColor = getColor(R.styleable.SunPosition_dash_color, Color.BLACK)
                lineColor = getColor(R.styleable.SunPosition_line_color, Color.YELLOW)
                bitmapSize = getDimension(R.styleable.SunPosition_bmp_size, 60f)
            }finally {
                recycle()
            }
        }

        init()
    }

    fun init(){
        dashedLinePaint.apply {
            color = dashColor
            strokeWidth = 20f
            style = Paint.Style.STROKE
            isAntiAlias = true
            pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
        }

        linePaint.apply {
            color = lineColor
            strokeWidth = 20f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val size = 5f
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )
        val customTypeface =
            ResourcesCompat.getFont(context, R.font.audiowide)

        textPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outerRect = RectF(padding - offset, padding - offset, width.toFloat() - padding + offset, (height.toFloat() - padding)*2 + offset)
        arcRect = RectF(padding, padding, width.toFloat() - padding, (height.toFloat() - padding)*2)
        ellipse = Ellipse(width.toFloat() - padding, (height.toFloat() - padding)*2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        when(getState(sunrise, sunset, current)){
            SunState.State1->{
                drawState1(canvas, ellipse)
            }
            SunState.State2->{
                drawState2(canvas, ellipse)
            }
            SunState.State3->{
                drawState3(canvas, ellipse)
            }
        }
    }

    fun display(sunrise : Long, sunset : Long){
        this.sunrise = sunrise
        this.sunset = sunset

        this.current = System.currentTimeMillis()/1000

        invalidate()
    }

    private fun drawState1(canvas : Canvas?, ellipse : Ellipse){
        val distance : Long = this.sunset - this.sunrise
        val centerX = distance/2f
        val x = ((this.current - this.sunrise) - centerX) * (ellipse.a.toFloat()/centerX)
        val arc = (ellipse.getArcFromX(x).toDouble() * 180.0/ Math.PI).toFloat()

        val sweep = if(arc > 0.0f)
        {
            90f + (90f - arc)
        }else
        {
            abs(arc)
        }

        val srText = formatDate(Date(this.sunrise*1000), "hh:mm a")
        val ssText = formatDate(Date(this.sunset*1000), "hh:mm a")

        canvas!!.apply {
            drawArc(arcRect, 180f, 180f, false, dashedLinePaint)
            drawArc(outerRect, 180f, sweep, false, linePaint)
            textPaint.getTextBounds(srText, 0, srText.length, bounds)
            drawText(srText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)
            textPaint.getTextBounds(ssText, 0, ssText.length, bounds)
            drawText(ssText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
        }
    }

    private fun drawState2(canvas: Canvas?, ellipse: Ellipse){
        val midnight = DateTimeUtils.atEndDate(this.current * 1000) / 1000
        val distance = midnight - this.sunset
        val centerX = distance/2f
        val x = ((this.current - this.sunset) - centerX) * (ellipse.a.toFloat()/centerX)
        val arc = (ellipse.getArcFromX(x).toDouble() * 180.0/ Math.PI).toFloat() + 90f
        val sweep = if(arc > 0.0f)
        {
            90f + (90f - arc)
        }else
        {
            abs(arc)
        }

        val leftText = formatDate(Date(this.sunset*1000), "hh:mm a")
        val rightText = "11:59 PM "

        canvas!!.apply {
            drawArc(arcRect, 180f, 180f, false, dashedLinePaint)
            drawArc(outerRect, 180f, sweep, false, linePaint)
            textPaint.getTextBounds(leftText, 0, leftText.length, bounds)
            drawText(leftText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)
            textPaint.getTextBounds(rightText, 0, rightText.length, bounds)
            drawText(rightText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
        }
    }

    private fun drawState3(canvas: Canvas?, ellipse: Ellipse){
        val midnight = DateTimeUtils.atStartDate(this.current * 1000) / 1000

        val distance = this.sunrise - midnight
        val centerX = distance/2f
        val x = ((this.current - midnight) - centerX) * (ellipse.a.toFloat()/centerX)
        val arc = (ellipse.getArcFromX(x).toDouble() * 180.0/ Math.PI).toFloat() + 90f
        val sweep = if(arc > 0.0f)
        {
            90f + (90f - arc)
        }else
        {
            abs(arc)
        }

        val leftText = "00:00 AM"
        val rightText = formatDate(Date(this.sunrise*1000), "hh:mm a")

        canvas!!.apply {
            drawArc(arcRect, 180f, 180f, false, dashedLinePaint)
            drawArc(outerRect, 180f, sweep, false, linePaint)
            textPaint.getTextBounds(leftText, 0, leftText.length, bounds)
            drawText(leftText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)
            textPaint.getTextBounds(rightText, 0, rightText.length, bounds)
            drawText(rightText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
        }
    }
}