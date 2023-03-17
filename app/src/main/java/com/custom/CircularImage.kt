package com.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.app.ngn.R
import kotlin.math.min

class CircularImage : View {
    private var src : Bitmap? = null
    private var r : Float = 0f
    private lateinit var rectF : RectF
    private lateinit var insideRectF : RectF
    private var padding : Float = 0f
    private var colorInt : Int = Color.BLACK
    private lateinit var paint : Paint
    private lateinit var bmpPaint : Paint
    private lateinit var cd : Paint

    constructor(context: Context?) : super(context)
    {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    {
        context!!.theme.obtainStyledAttributes(attrs, R.styleable.CircularImage, 0, 0).apply {
            try {
                padding = getDimension(R.styleable.CircularImage_line_width, 0f)
                colorInt = getColor(R.styleable.CircularImage_line_color, Color.BLACK)
                val drawable = getDrawable(R.styleable.CircularImage_src)
                src = (drawable as BitmapDrawable).bitmap
            }finally {
                recycle()
            }
        }

        init()
    }

    private fun init()
    {
        paint = Paint()
        paint.apply {
            isAntiAlias = true
            strokeWidth = 10f
            style = Paint.Style.FILL
            color = Color.BLACK
        }

        bmpPaint = Paint()
        bmpPaint.apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }

        cd = Paint()
        cd.apply {
            setLayerType(LAYER_TYPE_HARDWARE, cd)
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val cx = w/2f
        val cy = h/2f
        this.r = min(cx, cy)
        this.rectF = RectF(cx -r + padding/2, cy - r + padding/2, cx + r - padding/2, cy + r - padding/2)
        this.insideRectF = RectF(cx -r + padding, cy - r + padding, cx + r - padding, cy + r - padding)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.apply {
            drawRect(0f, 0f , width.toFloat(), height.toFloat(), cd)
            drawArc(insideRectF, 0f, 360f, false, paint)

            paint.apply {
                color = colorInt
                strokeWidth = padding
                style = Paint.Style.STROKE
            }

            if(src !=null){
                drawBitmap(src!!,null,insideRectF, bmpPaint)
            }
            drawArc(rectF, 0f, 360f, false, paint)
        }
    }

    fun setDrawableBitmap(src : Int){
        this.src = BitmapFactory.decodeResource(context.resources,src)
        invalidate()
    }
}