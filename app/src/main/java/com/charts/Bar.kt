package com.charts

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.app.ngn.R
import com.charts.data.DataSet
import com.general.NumberUtils
import kotlin.math.max
import kotlin.math.min

class Bar : View {
    enum class Type{
        Single,
        Stacked,
        Grouped;
        companion object
        {
            fun fromString(str : String) : Type
            {
                return when(str){
                    "stacked"->{
                        Stacked
                    }
                    "grouped"->{
                        Grouped
                    }
                    else->{
                        Single
                    }
                }
            }
        }
    }

    private lateinit var linePaint: Paint
    private lateinit var fillPaint : Paint
    private lateinit var textPaint : Paint
    private lateinit var chartType : Type
    private var displayText : Boolean = false

    private var radius : Float = 20f

    private var lx : Int = 0
    private var ly : Float = 0f

    private var data : List<DataSet> = getDefault()
        set(value) {
            field = value
            invalidate()
        }

    private var lineWidth = 5f
    private var barWidth = 40f
    private val textBoundHolder : Rect = Rect()

    private var padding : Float = 20f

    private var showAxes : Boolean = true

    private val colorList = arrayListOf(
        Color.BLUE,
        Color.YELLOW,
        Color.RED,
        Color.GREEN,
        Color.MAGENTA,
    )

    private lateinit var linePath : Path

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.Bar, 0, 0).apply {
            try {
                showAxes = getBoolean(R.styleable.Bar_showAxes, false)
                displayText = getBoolean(R.styleable.Bar_displayText, false)
                val str = getString(R.styleable.Bar_type)?:"single"
                chartType = Type.fromString(str)
            }finally {
                recycle()
            }
        }

        init()
    }

    private fun getDefault() : List<DataSet>
    {
        return arrayListOf(
            DataSet("DataSet 1", arrayListOf(10, 20, 40, 30, 10)),
            DataSet("DataSet 2", arrayListOf(100, 20, 30, 10, 60)),
            DataSet("DataSet 3", arrayListOf(5, 150, 70, 40, 90))
        )
    }

    fun addDataSet(set : DataSet)
    {
        val temp = this.data.toMutableList()
        temp.add(temp.size, set)

        this.data = temp
    }

    fun pop()
    {
        val temp = this.data.toMutableList()

        if(temp.size < 1){
            return
        }

        temp.removeAt(temp.size - 1)

        this.data = temp
    }

    private fun init(){
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.apply {
            color = Color.GREEN
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
        }

        fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        fillPaint.apply {
            color = Color.GREEN
            strokeWidth = 10f
            style = Paint.Style.FILL
        }

        val size = 3f
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            size, resources.displayMetrics
        )
        val customTypeface =
            ResourcesCompat.getFont(context, R.font.audiowide)

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.apply {
            color = Color.BLACK
            this.textSize = textSize
            typeface = customTypeface
        }

        linePath = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.ly = 0f
        data.forEach{ set->
            lx = max(lx, set.data.size)
            ly = max(ly, NumberUtils.max(set.data.map {
                it.toFloat()
            }).toFloat())
        }

        when(this.chartType){
            Type.Grouped->{
                group(canvas)
            }
            Type.Stacked->{
                stack(canvas)
            }
            else->{
                //Change later
                group(canvas)
            }
        }

        /*maxY = data.maxOf {
            it.
        }

        barWidth = 3*((width - 2*padding)/data.size)/5
        distance = ((width - 2*padding)/data.size)/5

        if(showAxes){
            canvas!!.apply {
                canvas.drawLine(padding, padding, padding, height.toFloat() - padding, axPaint)
                canvas.drawLine(padding,height.toFloat()- padding , width.toFloat() - padding, height.toFloat()- padding, axPaint)
            }
        }

        data.forEachIndexed{ i, d ->
            run {
                barPaint.apply {
                    strokeWidth = barWidth
                    color = Color.parseColor(colorList[i])
                }
                val x = convertX(i)
                val y = convertY(d.toDouble(), maxY)
                canvas!!.drawLine(x, height-padding, x , y, barPaint)
            }
        }*/
    }

    private fun drawBar(canvas: Canvas?, x: Float, y: Float, path: Path) {
        canvas ?: return

        val ratio = (this.height - 60f)/ ly
        val canvasY = this.height - y * ratio

        path.reset()
        path.moveTo(x - this.barWidth / 2f, this.height + 0f)
        path.lineTo(x - this.barWidth / 2f, canvasY)
        path.lineTo(x + this.barWidth / 2f, canvasY)
        path.lineTo(x + this.barWidth / 2f, this.height + 0f)
        path.close()

        canvas.drawPath(path, linePaint)

        path.lineTo(x - this.barWidth / 2f, this.height + 0f)
        path.close()

        fillPaint.color = Color.argb(100, linePaint.color.red, linePaint.color.green, linePaint.color.blue)
        canvas.drawPath(path, fillPaint)

        if(displayText)
        {
            val s = y.toInt().toString()
            textPaint.getTextBounds(s, 0, s.length, textBoundHolder)
            canvas.drawText(s, x - textBoundHolder.width()/2f , canvasY - 10f, textPaint)
        }
    }

    private fun group(canvas: Canvas?)
    {
        canvas?:return

        if(data.size > colorList.size)
        {
            return
        }

        val parts = (this.width - 20f)/lx

        this.padding = parts/4f
        this.barWidth = (3*parts/4f)/data.size
        this.lineWidth = min(this.barWidth/20f, this.lineWidth)

        linePaint.strokeWidth = this.lineWidth
        data.forEachIndexed { n, set ->
            linePaint.color = colorList[n]
            var currentX = n * this.barWidth + n * this.lineWidth
            set.data.forEachIndexed { i, d ->
                currentX += if(i > 0) {
                    parts
                }else {
                    20f + this.barWidth/2f
                }

                drawBar(canvas, currentX, d.toFloat(), linePath)
            }
        }
    }

    private fun drawStack(canvas: Canvas?, x: Float, y: Float, path: Path) : Float
    {
        canvas?:return 0f

        drawBar(canvas, x, y, path)

        return this.height - y * ((this.height - 100f)/ ly)
    }

    private fun stack(canvas: Canvas?)
    {
        canvas?:return
        val stackPos = arrayListOf<Float>()

    }
}