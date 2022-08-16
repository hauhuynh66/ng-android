package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Sport : ViewModel() {
    val state = MutableLiveData(0)
    val arg = MutableLiveData("")
}