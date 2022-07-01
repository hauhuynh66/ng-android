package com.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

class Animation {
    companion object{
        fun crossfade(v1: ArrayList<View>, v2:ArrayList<View>, duration: Long = 0) {
            v1.forEach {
                it.apply {
                    alpha = 0f
                    visibility = View.VISIBLE

                    animate()
                        .alpha(1f)
                        .setDuration(duration)
                        .setListener(null)
                }
            }

            v2.forEach{
                it.animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            it.visibility = View.GONE
                        }
                    })
            }
        }
    }
}