package com.app.data

data class FootballStandingData(val team : FootballTeam,
                                val match : MatchData,
                                val score : ScoreData,
                                val form: String ) {
    data class MatchData(val matchPlayed: Int, val matchWin : Int, val matchLose : Int, val matchDraw : Int)
    data class ScoreData(val scored : Int, val conceded : Int)
}