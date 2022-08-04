package com.app.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel

class AudioModel(var max : Long) : ViewModel() {
    private lateinit var timer : CountDownTimer

    fun startTimer(){
        timer = object : CountDownTimer(1000, 1000) {
            override fun onTick(p0: Long) {
                TODO("Not yet implemented")
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }
        }
    }
}