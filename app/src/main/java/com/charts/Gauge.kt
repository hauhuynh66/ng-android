package com.charts

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.app.ngn.R
import kotlin.math.min

class Gauge : View {
    enum class GaugeDisplay{
        LINE,
        ARC
    }
    var progress : Number = 40

    private var bgColor : Int = Color.GRAY
    private var fillColor : Int = Color.BLUE

    private lateinit var clrPaint : Paint
    private lateinit var textPaint: Paint

    private var padding : Float = 20f
    private var lineWidth : Float = 40f
    private var displayType = GaugeDisplay.LINE
    private var displayText = false

    private var offset : Float = 10f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.Gauge, 0, 0).apply {
            try {
                val type = getInt(R.styleable.Gauge_display, 0)
                displayType = when(type){
                    1->{
                        GaugeDisplay.ARC
                    }
                    else->{
                        GaugeDisplay.LINE
                    }
                }
                displayText = getBoolean(R.styleable.Gauge_displayText, true)
                bgColor = getInt(R.styleable.Gauge_background_color, Color.GRAY)
                fillColor = getInt(R.styleable.Gauge_progress_color, Color.BLUE)

            }finally {
                recycle()
            }
        }
    }

    private fun init(){
        clrPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clrPaint.apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
        }

        val size = 5f
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )

        val customTypeface =
            ResourcesCompat.getCachedFont(context!!, R.font.audiowide)

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when(displayType){
            GaugeDisplay.ARC ->{
                drawArc(canvas)
            }
            else->{
                drawLine(canvas)
            }
        }
    }

    private fun drawLine(canvas: Canvas?){
        val bounds = Rect()
        canvas!!.apply {
            clrPaint.color = bgColor
            drawLine(padding, height/2f, width-2*padding, height/2f, clrPaint)

            val x = (width-2*padding)*progress.toFloat()/100

            clrPaint.color = fillColor
            drawLine(padding, height/2f - offset, x, height/2f - offset, clrPaint)

            if(displayText) {
                val text = "$progress%"
                textPaint.getTextBounds(text, 0, text.length, bounds)
                drawText(text, width/2f - bounds.width()/2,  height/2f - lineWidth/2f - 10f , textPaint)
            }
        }
    }

    private fun drawArc(canvas: Canvas?){
        val bounds = Rect()
        val r = min(width, height)/2f - lineWidth/2f
        val cx = width/2f
        val cy = height - r/2
        val rectF = RectF(cx - r, cy-r, cx + r , cy+r)
        canvas!!.apply {
            clrPaint.color = bgColor
            drawArc(rectF, -180f, 180f, false, clrPaint)

            val angle = 1.8f * progress.toFloat()
            clrPaint.color = fillColor
            drawArc(rectF, -180f, angle, false, clrPaint)

            if(displayText) {
                val text = "$progress%"
                textPaint.getTextBounds(text, 0, text.length, bounds)
                drawText(text, width/2f - bounds.width()/2,  cy - 10f , textPaint)
            }
        }
    }

    fun setData(data : Number){
        this.progress = data
        invalidate()
    }
}