package com.app.viewmodel

import androidx.lifecycle.ViewModel
import com.app.data.FootballResult
import com.app.data.FootballTeam

class FootballDisplay : ViewModel() {
    var team : FootballTeam? = null
    var matchOverview : FootballResult? = null
}