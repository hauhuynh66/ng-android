package com.app.util

import com.app.data.FootballStandingData
import com.app.data.FootballTeam
import org.json.JSONObject

class FootballJson {
    companion object{
        fun getTeam(teamObj : JSONObject) : FootballTeam{
            val id = teamObj.getInt("id")
            val name = teamObj.getString("name")
            val icon = teamObj.getString("logo")
            return FootballTeam(id, name, icon)
        }

        fun getScore(scoreObj : JSONObject) : FootballStandingData.ScoreData{
            val goalScored = scoreObj.getInt("for")
            val goalConceded = scoreObj.getInt("against")
            return FootballStandingData.ScoreData(goalScored, goalConceded)
        }

        fun getMatch(allObj : JSONObject) : FootballStandingData.MatchData{
            val played = allObj.getInt("played")
            val win = allObj.getInt("win")
            val lose = allObj.getInt("lose")
            val draw = allObj.getInt("draw")
            return FootballStandingData.MatchData(played, win, lose, draw)
        }
    }
}