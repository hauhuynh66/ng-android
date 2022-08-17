package com.app.activity.sport

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.data.FootballResult
import com.app.data.FootballTeam
import com.app.fragment.sport.FootballResultFragment
import com.app.fragment.sport.SportListFragment
import com.app.ngn.R
import com.app.viewmodel.Sport
import org.json.JSONObject

class SportMainActivity : AppCompatActivity() {
    private val baseUrl : String = "https://v3.football.api-sports.io/fixtures"
    private val apiKey : String = "207de13e407253dfbe98859d90d378ce"
    private val apiHost : String = "v3.football.api-sports.io"
    private lateinit var requestQueue : RequestQueue

    private val stateModel : Sport by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)
        requestQueue = Volley.newRequestQueue(this)

        findViewById<TextView>(R.id.title).apply {
            text = "Sport"
        }

        stateModel.state.observe(this){
            when(it){
                1->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, FootballResultFragment()).commit()
                }
                else->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, SportListFragment()).commit()
                }
            }
        }

        stateModel.selectedDate.observe(this){
            getFootballResult(stateModel.selectedDate.value!!, "39")
        }
    }

    private fun getFootballResult(strDate : String, strLeagueId : String){
        val url = this.baseUrl + "?league=" + strLeagueId + "&date=" + strDate + "&season=" + 2022
        val fbRequest = object : StringRequest(Method.GET, url,
            {
                processFootballResult(it)
            },
            {
                println(it.message)
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["x-rapidapi-host"] = apiHost
                params["x-rapidapi-key"] = apiKey
                return params
            }
        }
        this.requestQueue.add(fbRequest)
    }

    private fun processFootballResult(json : String){
        val resArray = JSONObject(json).getJSONArray("response")
        val arr = arrayListOf<FootballResult>()
        for(i in 0 until resArray.length()){
            val obj = resArray.getJSONObject(i)
            val referee = obj.getJSONObject("fixture").getString("referee")
            val teams = obj.getJSONObject("teams")
            val homeTeam = FootballTeam(teams.getJSONObject("home").getString("name"),
                teams.getJSONObject("home").getString("logo"))
            val awayTeam = FootballTeam(teams.getJSONObject("away").getString("name"),
                teams.getJSONObject("away").getString("logo"))
            val homeScore = obj.getJSONObject("goals").getInt("home")
            val awayScore = obj.getJSONObject("goals").getInt("away")
            arr.add(FootballResult(homeTeam, awayTeam, referee, homeScore, awayScore))
        }

        stateModel.results.value = arr
    }
}