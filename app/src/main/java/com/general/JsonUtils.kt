package com.general

import com.app.data.score.FootballStanding
import com.app.data.score.FootballTeam
import org.json.JSONObject

class JsonUtils {
    companion object{
        fun getTeam(teamObj : JSONObject) : FootballTeam {
            val id = teamObj.getInt("id")
            val name = teamObj.getString("name")
            val icon = teamObj.getString("logo")
            return FootballTeam(id, name, icon)
        }

        fun getScore(scoreObj : JSONObject) : FootballStanding.ScoreData{
            val goalScored = scoreObj.getInt("for")
            val goalConceded = scoreObj.getInt("against")
            return FootballStanding.ScoreData(goalScored, goalConceded)
        }

        fun getMatch(allObj : JSONObject) : FootballStanding.MatchData{
            val played = allObj.getInt("played")
            val win = allObj.getInt("win")
            val lose = allObj.getInt("lose")
            val draw = allObj.getInt("draw")
            return FootballStanding.MatchData(played, win, lose, draw)
        }
    }
}