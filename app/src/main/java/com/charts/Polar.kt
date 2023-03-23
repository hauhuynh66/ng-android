package com.charts

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.app.ngn.R
import com.charts.data.DataSet
import com.general.NumberUtils
import kotlin.math.min

class Polar : View {
    var data : List<DataSet> = getDefault()
        set(value) {
            field = value
            this.invalidate()
        }

    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint

    private var colorList : List<Int> = arrayListOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.MAGENTA,
        Color.YELLOW
    )

    private var step : Int = 5
    private var rectF : RectF = RectF()
    private var defaultRect : RectF = RectF()
    private var center : Point = Point(0,0)
    private var outerR : Float = 0f

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.Polar, 0, 0).apply {
            try {
                step = getInteger(R.styleable.Polar_step, 5)
            }finally {
                recycle()
            }
        }
        init()
    }

    private fun getDefault(): List<DataSet> {
        return arrayListOf(
            DataSet("DataSet 1", arrayListOf(20, 40, 60, 10, 100))
        )
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
        canvas?:return

        this.rectF = defaultRect
        canvas.drawArc(rectF, 0f, 360f, false, linePaint)

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

        this.data.forEach { set ->
            val arc = 360f/set.data.size
            var currentSweepAngle = -90f

            val max = NumberUtils.max(set.data)

            set.data.forEachIndexed { i, value ->
                val r = if(value.toFloat() < max.toFloat()){
                    (value.toFloat()/max.toFloat()) * this.outerR
                }else{
                    this.outerR
                }

                rectF.apply {
                    left = center.x - r
                    top = center.y - r
                    right = center.x + r
                    bottom= center.y + r
                }

                sweepPaint.apply {
                    val j = if(i >= colorList.size)
                    {
                        i.mod(colorList.size)
                    }else{
                        i
                    }

                    color = Color.argb(100, colorList[j].red, colorList[j].green, colorList[j].blue)
                }
                canvas.drawArc(rectF, currentSweepAngle, arc, true, sweepPaint)
                currentSweepAngle+=arc
            }
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        center.set(w/2, h/2)

        this.outerR = (min(w.toFloat(), h.toFloat())/2f) - linePaint.strokeWidth

        this.defaultRect = RectF(
            center.x - this.outerR,
            center.y - this.outerR,
            center.x + this.outerR,
            center.y + this.outerR
        )
    }
}