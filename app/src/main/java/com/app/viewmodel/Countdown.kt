package com.app.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Countdown : ViewModel() {
    private val _current : LiveData<Int> = MutableLiveData(0)
    private lateinit var countDownTimer: CountDownTimer
}