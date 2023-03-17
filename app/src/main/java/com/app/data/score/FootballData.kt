package com.app.data.score

import androidx.lifecycle.ViewModel
import com.general.DateTimeUtils.Companion.formatDate
import java.util.*

enum class ScoreFlag{
    Fixture,
    Match,
    Select,
    Team,
    Standing
}

class ScoreContextHolder : ViewModel(){
    public var flag : ScoreFlag = ScoreFlag.Fixture
    public var leagueId : Int = 39
    public var season : Int = 2023
    public var selectedDate = formatDate(Date(), "yyyy-MM-dd")
    public var selectedTeamId : Int? = null
    public var currentMatchOverview : FootballResult? = null
}


