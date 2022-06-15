package com.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

class Animation {
    companion object{
        fun crossfade(v1:View, v2:View, duration: Long) {
            v1.apply {
                alpha = 0f
                visibility = View.VISIBLE

                animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setListener(null)
            }

            v2.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        v2.visibility = View.GONE
                    }
                })
        }
    }
}