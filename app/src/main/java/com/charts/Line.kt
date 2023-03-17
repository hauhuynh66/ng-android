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
import com.charts.data.DisplayOption
import com.general.NumberUtils
import kotlin.math.max

class Line : View{
    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet){
        init()
        context!!.theme.obtainStyledAttributes(attributeSet, R.styleable.Line, 0, 0).apply {
            try {
                showAxes = getBoolean(R.styleable.Line_showAxes, false)
                showPoints = getBoolean(R.styleable.Line_showPoints, true)
                isFilled = getBoolean(R.styleable.Line_fill, false)
            }finally {
                recycle()
            }
        }
    }

    private lateinit var linePath : Path
    private lateinit var displayLine : Path

    private lateinit var linePaint : Paint
    private lateinit var fillPaint : Paint

    private var pointWidth : Float = 20f
    private var pointRectF : RectF = RectF()
    private var pointTouchRadius : Float = 40f
    private var coordinates : List<List<PointF>> = arrayListOf()

    private var showAxes = true
    private var showPoints = true
    private var isFilled = false

    private var preferredMax = 0f

    private var padding : Float = 10f

    private var data : List<DataSet> = getDefault()
        set(value){
            field = value
            invalidate()
        }

    private fun getDefault() : List<DataSet>
    {
        return arrayListOf(
            DataSet("Set 1", arrayListOf(10, 20, 40, 30),
                DisplayOption(Color.rgb(255, 0, 0))),
            DataSet("Set 2", arrayListOf(40, 20, 10, 30),
                DisplayOption(Color.rgb(0, 0, 255)))
        )
    }

    fun addDataset(dataSet: DataSet)
    {
        val temp = this.data.toMutableList()
        temp.add(dataSet)

        this.data = temp
        invalidate()
    }

    fun pop()
    {
        val temp = this.data.toMutableList()
        temp.removeAt(temp.size - 1)

        this.data = temp
        invalidate()
    }

    private fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.GREEN
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

        fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        fillPaint.apply {
            color = Color.argb(100, 0, 255, 0)
            style = Paint.Style.FILL
        }

        linePath = Path()
        displayLine = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        preferredMax = getGlobalMax()
        coordinates = getCoordinates(preferredMax)

        if(showAxes)
        {
            format(canvas!!)
        }

        this.coordinates.forEachIndexed { i, set ->
            run {
                if(data[i].options!=null)
                {
                    val color = data[i].options!!.color
                    linePaint.color = color
                    fillPaint.color = Color.argb(100, color.red, color.green, color.blue)
                }

                linePath.reset()
                displayLine.reset()

                linePath.moveTo(set[0].x, set[0].y)
                for(pos in 1 until set.size){
                    val controlPoints = cubicControl(set[pos - 1], set[pos])

                    linePath.cubicTo(
                        controlPoints.first.x, controlPoints.first.y,
                        controlPoints.second.x, controlPoints.second.y,
                        set[pos].x, set[pos].y
                    )
                }

                displayLine.addPath(linePath)

                linePath.lineTo(set[set.size-1].x, height-padding)
                linePath.lineTo(set[0].x, height - padding)
                linePath.lineTo(set[0].x, set[0].y)
                linePath.close()

                canvas!!.drawPath(displayLine, linePaint)

                if(isFilled){
                    canvas.drawPath(linePath, fillPaint)
                }

                if(showPoints)
                {
                    drawPoints(set, canvas)
                }
            }

        }
    }

    private fun convertX(i : Int, n : Int) : Float{
        val x = (width-2*padding)/ n
        return padding + (i+1) * x
    }

    private fun convertY(y : Float, diffY : Float) : Float{
        val ratio = (height-2*padding)/diffY
        return (height - padding - y * ratio)
    }

    private fun getGlobalMax() : Float
    {
        var globalMax = 0f
        this.data.forEach { set ->
            run {
                val localMax = NumberUtils.max(set.data.map {
                    it.toFloat()
                }).toFloat()

                globalMax = max(globalMax, localMax)
            }
        }
        return globalMax
    }

    private fun getCoordinates(max : Float) : List<List<PointF>>
    {
        val ret = mutableListOf<List<PointF>>()
        this.data.forEach { set ->
            run {
                val nData = set.data.map {
                    it.toFloat()
                }
                val data : MutableList<PointF> = mutableListOf()

                data.add(PointF(padding, convertY(nData[0], max)))

                for(pos in 1 until nData.size){
                    val x = convertX(pos, nData.size)
                    val y = convertY(nData[pos], preferredMax)
                    data.add(PointF(x, y))
                }
                ret.add(data)
            }
        }
        return ret
    }

    private fun drawPoints(coordinates : List<PointF>, canvas: Canvas)
    {
        coordinates.forEach { point ->
            pointRectF.apply {
                left = point.x - pointWidth/2f
                right = point.x + pointWidth/2f
                top = point.y - pointWidth/2f
                bottom = point.y + pointWidth/2f
            }
            canvas.drawArc(pointRectF, 0f, 360f, false, fillPaint)
        }
    }

    private fun cubicControl(start : PointF, end : PointF) : Pair<PointF, PointF>
    {
        val x1 = PointF((start.x + end.x)/2f, start.y)
        val x2 = PointF((start.x + end.x)/2f, end.y)
        return Pair(x1, x2)
    }

    private fun format(canvas : Canvas)
    {
        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridPaint.apply {
            color = Color.BLACK
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

        canvas.drawLine(padding, padding, padding, height - padding, gridPaint)
        canvas.drawLine(padding, height-padding, width - padding, height-padding, gridPaint)
    }

    /**
     * Click + Press Event Handling
     * (Later)
     */
}