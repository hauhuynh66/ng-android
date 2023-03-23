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
    private var defaultRect : RectF = RectF()
    private var r : Float = 0f
    private var center : Point = Point(0, 0)

    private var colorList : List<Int> = arrayListOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.MAGENTA,
        Color.YELLOW
    )

    private var path : Path = Path()
    private var n = 6
    private var step = 5

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
        canvas?:return
        rectF = defaultRect

        val delta = this.r/step
        val sweep = 360f/n

        for(s in 0..step){
            var arc = -90f
            for(i in 0..n)
            {
                val j = if(i >= colorList.size)
                {
                    i.mod(colorList.size) + 1
                }else{
                    i
                }

                linePaint.color = colorList[j]
                canvas.drawArc(rectF, arc, sweep, false, linePaint)

                arc+=sweep
            }
            if(s>0)
            {
                rectF.apply {
                    top += delta
                    left += delta
                    bottom -= delta
                    right -= delta
                }
            }

        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.r = (min(w, h) - linePaint.strokeWidth)/2f

        this.center = Point(w/2, h/2)

        defaultRect.apply {
            left = center.x.toFloat() - r
            top = center.y.toFloat() - r
            right = center.x.toFloat() + r
            bottom = center.y.toFloat() + r
        }
    }


}