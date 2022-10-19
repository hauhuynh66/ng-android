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
    private var r : Float = 0f
    private var lineWidth = 10f
    private var padding = 20f
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
        init()
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0).apply {
            try {
                //shadow = getBoolean(R.styleable.PieChart_shadow, false)
                donut = getBoolean(R.styleable.PieChart_donut, false)
            }finally {
                recycle()
            }
        }
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

        colorList = generateColor(data.size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val centerX = width/2
        val centerY = height/2
        val r = min(width/2, height/2) - 2*padding

        outerRectF = RectF(centerX - r, centerY - r, centerX + r, centerY + r)
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
        if(donut){
            drawDonut(canvas, 2*r/3)
        }
    }
    private fun drawDonut(canvas: Canvas?, r : Float){
        val centerX = width/2
        val centerY = height/2
        val innerRectF = RectF(centerX - r, centerY - r, centerX + r, centerY + r)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.apply {
            color = Color.WHITE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        canvas!!.drawArc(innerRectF, 0f, 360f, true, paint)
    }

    fun setData(data : ArrayList<Number>){
        this.data = data
        this.colorList = generateColor(this.data.size)
        invalidate()
    }

    private fun generateColor(length : Int) : List<String>{
        val list = mutableListOf<String>()
        for(i in 0..length){
            list.add(Generator.generateColorCode())
        }
        return list
    }
}