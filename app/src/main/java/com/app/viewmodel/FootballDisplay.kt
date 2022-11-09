package com.app.viewmodel

import androidx.lifecycle.ViewModel
import com.football.FootballResult
import com.football.FootballTeam

class FootballDisplay : ViewModel() {
    var team : FootballTeam? = null
    var matchOverview : FootballResult? = null
}