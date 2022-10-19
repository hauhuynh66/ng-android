package com.app.view

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
    private lateinit var bgPaint : Paint
    private lateinit var pgPaint : Paint
    private lateinit var textPaint: Paint
    private var padding : Float = 20f
    private var lineWidth : Float = 20f
    private var displayType = GaugeDisplay.LINE
    private var displayText = false
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
                bgPaint.color = getColor(R.styleable.Gauge_background_color, Color.GRAY)
                pgPaint.color = getColor(R.styleable.Gauge_progress_color, Color.BLUE)
                displayText = getBoolean(R.styleable.Gauge_display_text, true)
            }finally {
                recycle()
            }
        }
    }

    private fun init(){
        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPaint.apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
        }

        pgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pgPaint.apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
        }

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when(displayType){
            GaugeDisplay.ARC->{
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
            drawLine(padding, height/2f, width-2*padding, height/2f, bgPaint)
            val x = (width-2*padding)*progress.toFloat()/100
            drawLine(padding, height/2f, x, height/2f, pgPaint)
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
            drawArc(rectF, -180f, 180f, false, bgPaint)
            val angle = 1.8f * progress.toFloat()
            drawArc(rectF, -180f, angle, false, pgPaint)
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