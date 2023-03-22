package com.charts

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import com.charts.data.DataSet
import com.general.Generator.Companion.generateColorCode
import com.general.NumberUtils
import kotlin.math.min

class Pie : View {
    private lateinit var sweepPaint : Paint
    private lateinit var cd : Paint

    private var rectF = RectF()
    private var center = PointF()
    private var r : Float = 0f
    private var range : Float = 0f
    private var defaultRect : RectF = RectF()

    private var padding = 20f
    private var weight = 0f

    private var data = getDefault()
        set(value) {
            field = value
            invalidate()
        }
    private var donut = false

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.Pie, 0, 0).apply {
            try {
                donut = getBoolean(R.styleable.Pie_donut, false)
            }finally {
                recycle()
            }
        }

        init()
    }

    private fun init(){
        sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sweepPaint.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }

        cd = Paint()
        cd.apply {
            setLayerType(LAYER_TYPE_HARDWARE, cd)
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    private fun getDefault() : List<DataSet>
    {
        return arrayListOf(
            DataSet("DataSet 1", arrayListOf(200, 300, 500, 400))
        )
    }

    fun addDataSet(dataSet : DataSet)
    {
        val temp = this.data.toMutableList()
        temp.add(dataSet)

        this.data = temp
        invalidate()
    }

    fun pop()
    {
        val temp = this.data.toMutableList()

        if(data.isEmpty())
        {
            return
        }

        temp.removeAt(temp.size - 1)
        this.data = temp
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.r = min(w, h)/2f - padding

        this.center = PointF(w/2f, h/2f)

        defaultRect = RectF(center.x - r, center.y - r, center.x + r, center.y + r)
    }

    override fun onDraw(canvas: Canvas?) {
        this.rectF.apply {
            left = defaultRect.left
            right = defaultRect.right
            bottom = defaultRect.bottom
            top = defaultRect.top
        }

        this.range = if(!donut){
            this.r
        }else
        {
            this.r/2f
        }

        this.weight = this.range / this.data.size

        this.data.forEachIndexed { i, set ->
            if(i > 0) {
                rectF.apply {
                    left += weight
                    top += weight
                    right -= weight
                    bottom -= weight
                }
            }

            //canvas!!.drawRect(0f, 0f , width.toFloat(), height.toFloat(), cd)

            val sum = NumberUtils.sum(set.data.map{
                it.toFloat()
            })
            var currentArc = -90f

            set.data.forEach {
                sweepPaint.color = Color.parseColor(generateColorCode())

                val sweepArc = (it.toFloat()/sum.toFloat()) * 360f
                canvas!!.drawArc(this.rectF, currentArc, sweepArc, true, sweepPaint)
                currentArc += sweepArc
            }
        }

        if (donut){
            this.rectF.apply {
                left = center.x - range
                right = center.x + range
                top = center.y - range
                bottom = center.y + range
            }
            canvas!!.drawArc(rectF, 0f, 360f, true, cd)
        }
    }

}