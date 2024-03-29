package com.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class DrawView : View {
    var mBitmap: Bitmap? = null
    var bg : Bitmap? = null
    private var mCanvas: Canvas? = null
    private var mPath: Path? = null
    private var mBitmapPaint = Paint(Paint.DITHER_FLAG)
    private var circlePaint =  Paint()
    private val mPaint = Paint()
    private var circlePath: Path
    private var c : Context? = null
    private var mX = 0f
    private  var mY = 0f
    private val tolerance = 4f
    private val savedPath : ArrayList<PathData> = arrayListOf()

    fun prev(){
        if(savedPath.size > 0){
            savedPath.removeAt(savedPath.size-1)
        }

        mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        if(bg!=null){
            mCanvas!!.drawBitmap(bg!!, 0f, 0f, mBitmapPaint)
        }

        for (p : PathData in savedPath){
            mCanvas!!.drawPath(p.path, p.paint)
        }
    }

    fun changeColor(color : Int){
        mPaint.apply {
            this.color = color
        }
        circlePaint.apply {
            this.color = color
        }
    }

    fun changePathWidth(width : Float){
        mPaint.apply {
            this.strokeWidth = width
        }
    }

    fun clearBackground(){
        bg = null
        mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        mCanvas!!.apply {
            for(p: PathData in savedPath){
                this.drawPath(p.path, p.paint)
            }
        }
    }

    fun changeBackground(bitmap: Bitmap){
        val stretched = Bitmap.createScaledBitmap(bitmap, width, height, false)
        bg = stretched
        mCanvas!!.apply {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            this.drawBitmap(stretched, 0f, 0f, mBitmapPaint)
            for(p: PathData in savedPath){
                this.drawPath(p.path, p.paint)
            }
        }
    }

    constructor(context: Context?) : super(context){
        c = context
        mPath = Path()
        mBitmapPaint = Paint(Paint.DITHER_FLAG)
        circlePaint = Paint()
        circlePath = Path()

        mPaint.apply {
            isAntiAlias = true
            isDither = true
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 12f
        }

        circlePaint.apply {
            isAntiAlias = true
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.MITER
            strokeWidth = 4f
        }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        c = context
        mPath = Path()
        mBitmapPaint = Paint(Paint.DITHER_FLAG)
        circlePaint = Paint()
        circlePath = Path()

        mPaint.apply {
            isAntiAlias = true
            isDither = true
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 12f
        }

        circlePaint.apply {
            isAntiAlias = true
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.MITER
            strokeWidth = 4f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
        canvas.drawPath(mPath!!, mPaint)
        canvas.drawPath(circlePath, circlePaint)
    }

    private fun touchStart(x: Float, y: Float) {
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy: Float = abs(y - mY)
        if (dx >= tolerance || dy >= tolerance) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
            circlePath.reset()
            val minWidth = if(12f>mPaint.strokeWidth){
                12f
            }else{
                mPaint.strokeWidth
            }
            circlePath.addCircle(mX, mY, minWidth, Path.Direction.CW)
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
        circlePath.reset()
        // commit the path to our offscreen
        mCanvas!!.drawPath(mPath!!, mPaint)
        val savedPaint = Paint(mPaint)
        if(mPaint.color == Color.WHITE){
            savedPaint.color = Color.TRANSPARENT
        }
        savedPath.add(PathData(Path(mPath), savedPaint))
        // kill this so we don't double draw
        mPath!!.reset()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    class PathData(val path: Path, val paint: Paint)
}