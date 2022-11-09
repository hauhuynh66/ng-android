package com.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Path
import android.view.View
import android.view.animation.PathInterpolator
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation

class Animation {
    companion object{
        fun crossfade(v1: View? = null, v2:View? = null, duration: Long = 0L) {
            v1?.apply {
                this.animate()
                    .alpha(1f)
                    .setDuration(duration/2L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            visibility = View.VISIBLE
                        }
                    })
            }

            v2?.apply{
                this.animate()
                    .alpha(0f)
                    .setDuration(duration/2L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            visibility = View.GONE
                        }
                    })
            }
        }

        fun translateX(view : View, value : Number, duration: Long){
            ObjectAnimator.ofFloat(view, "translationX", value.toFloat())
                .setDuration(duration)
                .start()
        }

        fun translateY(view : View, value : Number, duration: Long){
            ObjectAnimator.ofFloat(view, "translationY", value.toFloat())
                .setDuration(duration)
                .start()
        }

        fun pathAnimate(view : View, path : Path, duration: Long){
            val pathInterpolator = PathInterpolator(path)
            val objectAnimator = ObjectAnimator.ofFloat(view, "translationX")
            objectAnimator.apply {
                this.interpolator = pathInterpolator
                this.duration = duration
            }.start()
        }

        fun fling(view : View, property : DynamicAnimation.ViewProperty, maxValue : Number){
            val flingAnimator = FlingAnimation(view, property)
            flingAnimator.apply {
                setMinValue(0f)
                setMaxValue(maxValue.toFloat())
            }.start()
        }

    }
}