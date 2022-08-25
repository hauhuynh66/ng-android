package com.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

class Animation {
    companion object{
        fun crossfade(v1: ArrayList<View>, v2:ArrayList<View>, duration: Long = 0L) {
            v1.forEach {
                it.animate()
                    .alpha(1f)
                    .setDuration(duration/2L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            it.visibility = View.VISIBLE
                        }
                    })
            }

            v2.forEach{
                it.animate()
                    .alpha(0f)
                    .setDuration(duration/2L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            it.visibility = View.GONE
                        }
                    })
            }
        }
    }
}