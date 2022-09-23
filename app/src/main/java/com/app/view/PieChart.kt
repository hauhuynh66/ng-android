package com.app.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import kotlin.math.min
import kotlin.random.Random

class PieChart : View {
    private lateinit var linePaint : Paint
    private lateinit var sweepPaint : Paint
    private lateinit var outerRectF: RectF
    private var lineWidth = 10f
    private var padding = 20f
    private var data = arrayListOf<Number>(
        100, 200, 300, 500, 20, 400
    )
    private var shadow = false
    private var donut = false
    private lateinit var random : Random

    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0).apply {
            try {
                shadow = getBoolean(R.styleable.PieChart_shadow, false)
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
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX = width/2
        val centerY = height/2
        val r = min(width/2, height/2) - 2*padding
        outerRectF = RectF(centerX - r, centerY - r, centerX + r, centerY + r)

        val sum : Double = data.reduce{
            a,b -> a.toDouble() + b.toDouble()
        }.toDouble()

        var currentArc = -90f
        data.forEachIndexed {
            i, it -> run{
                random = Random(i)
                sweepPaint.color = Color.parseColor(randomColor())
                val sweepArc = ((it.toDouble()/sum)).toFloat() * 360f
                canvas!!.drawArc(outerRectF, currentArc, sweepArc, true, sweepPaint)
                currentArc += sweepArc
            }
        }
        if(donut){
            drawDonut(canvas, 2*r/3)
        }
    }

    private fun randomColor() : String{
        val chars = "0123456789"
        val ret = StringBuilder()
        ret.append("#")
        for(i in 0 until 6){
            ret.append(chars[random.nextInt(chars.length-1)])
        }
        return ret.toString()
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

    public fun setData(data : ArrayList<Number>){
        this.data = data
        invalidate()
    }
}