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
    private var defaultRect : RectF = RectF()

    private var padding = 20f
    private var weight = 0f

    private var data = getDefault()
        set(value) {
            field = value
            invalidate()
        }
    private var shadow = false
    private var donut = false

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.Pie, 0, 0).apply {
            try {
                shadow = getBoolean(R.styleable.Pie_shadow, false)
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
            DataSet("DataSet 1", arrayListOf(200, 300, 500, 400)),
            DataSet("DataSet 2", arrayListOf(50, 200, 300, 60))
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
        this.weight = this.r / this.data.size
        rectF.apply {
            left = defaultRect.left
            right = defaultRect.right
            bottom = defaultRect.bottom
            top = defaultRect.top
        }

        this.data.forEachIndexed { i, set ->
            if(i > 0) {
                rectF.apply {
                    left += weight
                    top += weight
                    right -= weight
                    bottom -= weight
                }
            }

            val sum = NumberUtils.sum(set.data.map{
                it.toFloat()
            })
            var currentArc = -90f
            //canvas!!.drawRect(0f, 0f , width.toFloat(), height.toFloat(), cd)

            set.data.forEach {
                sweepPaint.color = Color.parseColor(generateColorCode())

                val sweepArc = (it.toFloat()/sum.toFloat()) * 360f
                canvas!!.drawArc(rectF, currentArc, sweepArc, true, sweepPaint)
                currentArc += sweepArc
            }
        }
        /*val sum : Double = data.reduce{
            a,b -> a.toDouble() + b.toDouble()
        }.toDouble()

        var currentArc = -90f

        canvas!!.drawRect(0f, 0f , width.toFloat(), height.toFloat(), cd)

        data.forEachIndexed {
            i, it -> run{
                sweepPaint.color = Color.parseColor(colorList[i])
                val sweepArc = ((it.toDouble()/sum)).toFloat() * 360f
                canvas.drawArc(outerRectF, currentArc, sweepArc, true, sweepPaint)
                currentArc += sweepArc
            }
        }

        if (donut){
            canvas.drawArc(innerRectF, 0f, 360f, true, cd)
        }*/
    }

}