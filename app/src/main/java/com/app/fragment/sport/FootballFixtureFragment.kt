package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.app.adapter.FootballFixtureAdapter
import com.app.data.FootballResult
import com.app.data.FootballTeam
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.util.FootballAPI
import com.app.util.Formatter.Companion.formatDate
import com.app.viewmodel.Football
import org.json.JSONObject
import java.util.*

class FootballFixtureFragment : Fragment() {
    private val model : Football by activityViewModels()
    private val postfix : String = "/fixtures"
    private lateinit var result : ArrayList<FootballResult>
    private lateinit var progress : ProgressBar
    private lateinit var list : RecyclerView
    private lateinit var adapter: FootballFixtureAdapter
    private val calendar = GregorianCalendar()

    private fun getResult(strLeagueId : String, strDate : String){
        crossfade(arrayListOf(progress), arrayListOf(list))
        val url = model.baseUrl + postfix + "?league=" + strLeagueId + "&season=" + 2022 + "&date=" + strDate
        val fbRequest = object : StringRequest(
            Method.GET, url,
            {
                println(it)
                adapter.data = processFootballResult(it)
                adapter.notifyDataSetChanged()
                crossfade(arrayListOf(list), arrayListOf(progress))
            },
            {
                println(it.message)
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["x-rapidapi-host"] = model.apiHost
                params["x-rapidapi-key"] = model.apiKey
                return params
            }
        }
        fbRequest.tag = "FB-LIST"
        model.requestQueue.add(fbRequest)
    }

    private fun processFootballResult(json : String) : ArrayList<FootballResult>{
        val resArray = JSONObject(json).getJSONArray("response")
        val arr = arrayListOf<FootballResult>()
        for(i in 0 until resArray.length()){
            val obj = resArray.getJSONObject(i)
            val referee = obj.getJSONObject("fixture").getString("referee").substringBefore(",")
            val matchId = obj.getJSONObject("fixture").getLong("id")
            val teams = obj.getJSONObject("teams")
            val homeTeam = FootballAPI.getTeam(teams.getJSONObject("home"))
            val awayTeam = FootballAPI.getTeam(teams.getJSONObject("away"))
            val homeScore = if(obj.getJSONObject("goals").isNull("home")){
                null
            }else{
                obj.getJSONObject("goals").getInt("home")
            }

            val awayScore = if(obj.getJSONObject("goals").isNull("away")){
                null
            }else{
                obj.getJSONObject("goals").getInt("away")
            }

            arr.add(FootballResult(homeTeam, awayTeam, referee, homeScore, awayScore, matchId))
        }

        return arr
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fg_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.item_list)
        progress = view.findViewById(R.id.progress)
        result = arrayListOf()

        adapter = FootballFixtureAdapter(requireContext(), result, object : FootballFixtureAdapter.Callback{
            override fun onTeamClick(team: FootballTeam) {
                model.state.value = Football.State.TeamDetails
                model.selectedClub.value = team
            }

            override fun onClick(overview: FootballResult) {
                model.state.value = Football.State.MatchDetails
                model.selectedMatchOverview.value = overview
            }
        })

        model.currentDate.observe(requireActivity()){
            calendar.time = model.currentDate.value!!
            getResult("39", formatDate(calendar.time, "yyyy-MM-dd"))
        }

        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list.adapter = adapter
    }
}