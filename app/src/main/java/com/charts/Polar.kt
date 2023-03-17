package com.charts

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import com.charts.data.DataSet
import com.general.NumberUtils
import kotlin.math.min

class Polar : View {
    var data : MutableList<DataSet> = arrayListOf(
        DataSet("DataSet1", arrayListOf(30)),
        DataSet("DataSet2", arrayListOf(50)),
        DataSet("DataSet3", arrayListOf(70))
    )
        set(value) {
            field = value
            this.invalidate()
        }

    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint
    private var test : List<Int> = arrayListOf(
        Color.argb(150, 255, 0,0),
        Color.argb(150, 0, 255, 0),
        Color.argb(150, 0, 0, 255),
    )

    private var step : Int = 10
    private lateinit var rectF : RectF
    private var center : Point = Point(0,0)
    private var outerR : Float = 0f

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.Polar, 0, 0).apply {
            try {
                step = getInteger(R.styleable.Polar_step, 10)
            }finally {
                recycle()
            }
        }
        init()
    }

    fun init(){
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
        val max = NumberUtils.max(this.data.map {
            it.data[0]
        })

        canvas!!.drawArc(rectF, 0f, 360f, false, linePaint)
        for(i in 1 until step)
        {
            rectF.apply {
                left += outerR/step
                top += outerR/step
                right -= outerR/step
                bottom -= outerR/step
            }
            canvas.drawArc(rectF, 0f, 360f, false, linePaint)
        }

        val arc = 360f/data.size
        var currentSweepAngle = -90f

        this.data.forEachIndexed { i, it ->
            run {
                println((it.data[0].toFloat()/max.toFloat()))
                val r = (it.data[0].toFloat()/max.toFloat()) * outerR
                rectF.apply {
                    left = center.x - r
                    top = center.y - r
                    right = center.x + r
                    bottom= center.y + r
                }
                sweepPaint.apply {
                    color = test[i]
                }
                canvas!!.drawArc(rectF, currentSweepAngle, arc, true, sweepPaint)
                currentSweepAngle+=arc
            }
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.set(w/2, h/2)
        this.outerR = (min(w.toFloat(), h.toFloat()) /2f) - linePaint.strokeWidth
        this.rectF = RectF(
            center.x - this.outerR,
            center.y - this.outerR,
            center.x + this.outerR,
            center.y + this.outerR
        )
    }

    fun appendData(dataset : DataSet)
    {
        this.data.add(dataset)
        this.invalidate()
    }
}