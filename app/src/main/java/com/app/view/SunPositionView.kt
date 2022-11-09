package com.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.weather.SunState
import com.app.ngn.R
import com.app.util.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.*

class SunPositionView : View {
    private var sunset: Long = 0
    private var sunrise: Long = 0
    private var current: Long = 0

    private var dashColor : Int = Color.BLACK
    private var lineColor : Int = Color.YELLOW
    private var bitmapSize : Float = 60f

    private var arcRect : RectF = RectF()
    private val bounds : Rect = Rect()

    private var dashedLinePaint : Paint = Paint()
    private var linePaint : Paint = Paint()
    private var textPaint : Paint = Paint()
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
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.SunPositionView, 0, 0).apply {
            try {
                dashColor = getColor(R.styleable.SunPositionView_dash_color, Color.BLACK)
                lineColor = getColor(R.styleable.SunPositionView_line_color, Color.YELLOW)
                bitmapSize = getDimension(R.styleable.SunPositionView_bmp_size, 60f)
            }finally {
                recycle()
            }
        }

        init()
    }

    fun init(){
        dashedLinePaint.apply {
            color = dashColor
            strokeWidth = 6f
            style = Paint.Style.STROKE
            isAntiAlias = true
            pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
        }

        linePaint.apply {
            color = lineColor
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
            ResourcesCompat.getFont(context, R.font.audiowide)

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
        when(getState(sunrise, sunset, current)){
            SunState.State1->{
                drawState1(canvas)
            }
            SunState.State2->{
                drawState2(canvas)
            }
            SunState.State3->{
                drawState3(canvas)
            }
        }
    }

    fun display(sunrise : Long, sunset : Long){
        this.sunrise = sunrise
        this.sunset = sunset

        this.current = System.currentTimeMillis()/1000

        invalidate()
    }

    private fun format(date : Date) : String{
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }

    private fun drawState1(canvas : Canvas?){
        val dis: Long = sunset - sunrise
        val c: Long = current - sunrise
        val deg = (c.toDouble()/dis.toDouble()) * 180.0
        val srText = format(Date(this.sunrise*1000))
        val ssText = format(Date(this.sunset*1000))

        canvas!!.apply {
            drawArc(arcRect, 180f, 180f, false, dashedLinePaint)
            drawArc(arcRect, 180f, deg.toFloat(), false, linePaint)
            textPaint.getTextBounds(srText, 0, srText.length, bounds)
            drawText(srText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)
            textPaint.getTextBounds(ssText, 0, ssText.length, bounds)
            drawText(ssText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
        }
    }

    private fun drawState2(canvas: Canvas?){
        val midnight = DateTimeUtils.atEndDate(this.current * 1000) / 1000
        val distance = midnight - this.sunset
        val weight = this.current - this.sunset
        val deg = (weight.toDouble()/distance.toDouble()) * 180.0

        val leftText = format(Date(this.sunset*1000))
        val rightText = "11:59 PM "

        canvas!!.apply {
            drawArc(arcRect, 180f, 180f, false, dashedLinePaint)
            drawArc(arcRect, 180f, deg.toFloat(), false, linePaint)
            textPaint.getTextBounds(leftText, 0, leftText.length, bounds)
            drawText(leftText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)
            textPaint.getTextBounds(rightText, 0, rightText.length, bounds)
            drawText(rightText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
        }
    }

    private fun drawState3(canvas: Canvas?){
        val midnight = DateTimeUtils.atStartDate(this.current * 1000) / 1000
        val distance = this.sunrise - midnight
        val weight = this.current - midnight
        val deg = (weight.toDouble()/distance.toDouble()) * 180.0

        val leftText = "00:00 AM"
        val rightText = format(Date(this.sunrise*1000))

        canvas!!.apply {
            drawArc(arcRect, 180f, 180f, false, dashedLinePaint)
            drawArc(arcRect, 180f, deg.toFloat(), false, linePaint)
            textPaint.getTextBounds(leftText, 0, leftText.length, bounds)
            drawText(leftText, padding + 20f, height - bounds.height() / 2 - 10f, textPaint)
            textPaint.getTextBounds(rightText, 0, rightText.length, bounds)
            drawText(rightText, width - 40f - bounds.width(), height - bounds.height() / 2 - 10f, textPaint)
        }
    }
}