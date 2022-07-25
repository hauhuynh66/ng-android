package com.app.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min

class GaugeView : View {
    private var linePaint : Paint? = Paint()
    private var gaugePaint : Paint? = Paint()
    private var progress : Int = 50
    private var mAnimator : ValueAnimator? = null
    private var currentDeg : Float = 0f

    constructor(context: Context?) : super(context){
        linePaint!!.apply {
            color = Color.BLACK
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        gaugePaint!!.apply {
            color = Color.BLUE
            strokeWidth = 50f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        mAnimator = ValueAnimator.ofFloat(0f, 180f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener {
                currentDeg = mAnimator!!.animatedValue as Float
                invalidate()
            }
        }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        linePaint!!.apply {
            color = Color.BLACK
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        gaugePaint!!.apply {
            color = Color.BLUE
            strokeWidth = 50f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        mAnimator = ValueAnimator.ofFloat(0f, 180f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener {
                currentDeg = mAnimator!!.animatedValue as Float
                invalidate()
            }
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val padding = 20f
        val distance = 50f
        val r = min(width/2, height) - padding
        val r2 = r - distance
        val r3 = (r+r2) /2

        val cx = (width/2).toFloat()
        val cy = height.toFloat()
        val rectF = RectF(cx - r, cy - r, cx + r , cy + r)
        val iRectF = RectF(cx - r2, cy - r2, cx + r2, cy + r2)
        val gaugeRectF = RectF(cx - r3, cy - r3, cx + r3, cy + r3)
        canvas!!.apply {
            drawArc(rectF, -180f, 180f, false, linePaint!!)
            drawArc(iRectF, -180f, 180f, false, linePaint!!)
            drawLine(rectF.left, height.toFloat(), iRectF.left, height.toFloat(), linePaint!!)
            drawLine(rectF.right, height.toFloat(), iRectF.right, height.toFloat(), linePaint!!)
            val deg = (progress.toFloat()/100f) * 180f
            drawArc(gaugeRectF, -180f, deg, false, gaugePaint!!)
        }
    }

    fun setProgress(progress : Int){
        this.progress = progress
        invalidate()
    }
}