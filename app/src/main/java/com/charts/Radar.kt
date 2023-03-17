package com.charts

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class Radar : View {
    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint
    private var rectF : RectF = RectF()
    private var r : Float = 0f
    private var center : Point = Point(0, 0)
    private var path : Path = Path()
    //private var layers = 5f
    private var n = 6

    constructor(context: Context?) : super(context)
    {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    {
        init()
    }

    fun init()
    {
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.rgb(138, 143, 150)
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }

        sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sweepPaint.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.r = min(w, h)/2f
        this.center = Point(w/2, h/2)
        rectF.apply {
            left = center.x.toFloat() - r
            top = center.y.toFloat() - r
            right = center.x.toFloat() + r
            bottom = center.y.toFloat() + r
        }
    }


}