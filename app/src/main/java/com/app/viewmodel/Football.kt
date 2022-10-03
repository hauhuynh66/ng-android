package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.data.FootballResult
import com.app.data.FootballTeam
import java.util.*

class Football : ViewModel() {
    val currentDate = MutableLiveData(Date())
    val currentLeague = MutableLiveData(39)
}