package com.app.util

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.app.ngn.R
import kotlin.math.abs
import kotlin.math.max

class ViewUtils {
    companion object{
        /**
         *
         */
        class ZoomOutPageTransformer: ViewPager2.PageTransformer{
            private val minScale = 0.85f
            private val minAlpha = 0.5f
            override fun transformPage(page: View, position: Float) {
                page.apply {
                    val pageWidth = width
                    val pageHeight = height
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 1 -> {

                            val scaleFactor = max(minScale, 1 - abs(position))
                            val vertMargin = pageHeight * (1 - scaleFactor) / 2
                            val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
                            translationX = if (position < 0) {
                                horizontalMargin - vertMargin / 2
                            } else {
                                horizontalMargin + vertMargin / 2
                            }

                            scaleX = scaleFactor
                            scaleY = scaleFactor

                            alpha = (minAlpha +
                                    (((scaleFactor - minScale) / (1 - minScale)) * (1 - minAlpha)))
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }

        /**
         *
         */
        class DepthPageTransFormer : ViewPager.PageTransformer{
            private val minScale = 0.75f
            override fun transformPage(page: View, position: Float) {
                page.apply {
                    val pageWidth = width
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 0 -> {
                            alpha = 1f
                            translationX = 0f
                            scaleX = 1f
                            scaleY = 1f
                        }
                        position <= 1 -> {
                            alpha = 1 - position

                            translationX = pageWidth * -position

                            val scaleFactor = (minScale + (1 - minScale) * (1 - Math.abs(position)))
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }

        /**
         *
         */
        fun getFixedHorizontalLayoutManager(context: Context) : RecyclerView.LayoutManager{
            return object : LinearLayoutManager(context, HORIZONTAL, false){
                override fun generateLayoutParams(
                    c: Context?,
                    attrs: AttributeSet?
                ): RecyclerView.LayoutParams {
                    return resize(super.generateLayoutParams(c, attrs))
                }

                private fun getHorizontalSpace(): Int {
                    return width - paddingLeft - paddingRight
                }

                private fun resize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
                    layoutParams.width = getHorizontalSpace() / itemCount
                    return layoutParams
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        }

        /**
         *
         */
        fun getFixedVerticalLayoutManager(context: Context) : RecyclerView.LayoutManager{
            return object : LinearLayoutManager(context, VERTICAL, false){
                override fun generateLayoutParams(
                    c: Context?,
                    attrs: AttributeSet?
                ): RecyclerView.LayoutParams {
                    return spanLayoutSize(super.generateLayoutParams(c, attrs))
                }

                private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
                    layoutParams.height = (height - paddingBottom - paddingTop) / itemCount
                    return layoutParams
                }

                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        fun getFixedSpanLayoutManager(context: Context, spanCount : Int) : RecyclerView.LayoutManager{
            return object : GridLayoutManager(context, spanCount) {
                override fun generateLayoutParams(
                    c: Context?,
                    attrs: AttributeSet?
                ): RecyclerView.LayoutParams {
                    return spanLayoutSize(super.generateLayoutParams(c, attrs))
                }

                private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
                    val row = itemCount / spanCount
                    val mod = itemCount % spanCount
                    if(mod != 0){
                        row + 1
                    }

                    layoutParams.height = (height - paddingBottom - paddingTop) / row
                    layoutParams.width = (width - paddingLeft - paddingRight) / spanCount
                    return layoutParams
                }

                override fun canScrollVertically(): Boolean {
                    return false
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        }

        fun configTitle(toolbar : Toolbar, homeDisplay : Boolean, menuNumber : Int = 0){
            val constraintSet = ConstraintSet()
            constraintSet.clone(toolbar.findViewById<ConstraintLayout>(R.id.title_holder))
            val metrics = toolbar.context.resources.displayMetrics
            val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, metrics).toInt()

            val rightMargin = if(homeDisplay){
                size
            }else{
                0
            }

            val leftMargin = menuNumber * size

            constraintSet.connect(R.id.title, ConstraintSet.LEFT, R.id.title_holder, ConstraintSet.LEFT, leftMargin)
            constraintSet.connect(R.id.title, ConstraintSet.RIGHT, R.id.title_holder, ConstraintSet.RIGHT, rightMargin)

            constraintSet.applyTo(toolbar.findViewById(R.id.title_holder))
        }
    }

}