package com.app.data

class FootballResult(val homeTeam : FootballTeam,
                     val awayTeam: FootballTeam,
                     val referee : String,
                     val homeGoal : Int?,
                     val awayGoal : Int?,
                     val matchId : Long)