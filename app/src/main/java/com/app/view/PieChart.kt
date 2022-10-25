package com.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import com.app.util.Generator
import kotlin.math.min

class PieChart : View {
    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint
    private var outerRectF: RectF = RectF()
    private var innerRectF: RectF = RectF()
    private var random : Boolean = false
    private var lineWidth = 10f
    private var padding = 10f
    private var data = arrayListOf<Number>(
        100, 200, 300, 500, 20, 400
    )
    private var shadow = false
    private var donut = false
    private lateinit var colorList : List<String>

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0).apply {
            try {
                shadow = getBoolean(R.styleable.PieChart_shadow, false)
                donut = getBoolean(R.styleable.PieChart_donut, false)
                random = getBoolean(R.styleable.PieChart_random, false)
            }finally {
                recycle()
            }
        }

        colorList = Generator.generateColor(data.size)
        init()
    }

    private fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.GREEN
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
        }
        if(shadow){
            linePaint.setShadowLayer(12f, 0f, 0f, Color.YELLOW)
            setLayerType(LAYER_TYPE_SOFTWARE, linePaint)
        }

        sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sweepPaint.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val centerX = width/2
        val centerY = height/2
        val r = min(width/2, height/2) - 2*padding

        outerRectF = RectF(centerX - r, centerY - r, centerX + r, centerY + r)
        innerRectF = RectF(centerX - 2*r/3, centerY - 2*r/3, centerX + 2*r/3, centerY + 2*r/3)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val sum : Double = data.reduce{
            a,b -> a.toDouble() + b.toDouble()
        }.toDouble()

        var currentArc = -90f

        data.forEachIndexed {
            i, it -> run{
                sweepPaint.color = Color.parseColor(colorList[i])
                val sweepArc = ((it.toDouble()/sum)).toFloat() * 360f
                canvas!!.drawArc(outerRectF, currentArc, sweepArc, true, sweepPaint)
                currentArc += sweepArc
            }
        }

        if (donut){
            sweepPaint.apply {
                color = Color.WHITE
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            }

            canvas!!.drawArc(innerRectF, 0f, 360f, true, sweepPaint)
        }
    }

    fun setData(list : ArrayList<Number>){
        this.data = list
        colorList = Generator.generateColor(data.size)
        invalidate()
    }
}