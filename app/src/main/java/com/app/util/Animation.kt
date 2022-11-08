package com.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

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
    }
}