package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.app.adapter.StatAdapter
import com.app.data.FootballResult
import com.app.data.StatLineData
import com.app.ngn.R
import com.app.viewmodel.Football
import org.json.JSONObject

class FootballMatchDetailFragment : Fragment() {
    private val model : Football by activityViewModels()
    private val postfix = "/fixtures/statistics"
    private lateinit var adapter : StatAdapter
    private lateinit var data : ArrayList<StatLineData>
    private val stats =
        arrayListOf(
            "Shots on Goal", "Shots off Goal", "Total Shots", "Blocked Shots", "Shots insidebox", "Shots outsidebox",
            "Fouls", "Corner Kicks", "Offsides", "Ball Possession", "Yellow Cards", "Red Cards", "Goalkeeper Saves",
            "Total passes", "Passes accurate", "Passes %"
            )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_football_match_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        redisplayOverview(model.selectedMatchOverview.value!!, view.findViewById(R.id.overview_group))
        data = arrayListOf()
        adapter = StatAdapter(requireContext(), data)
        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list.adapter = adapter
        getMatchDetails()

    }

    private fun redisplayOverview(overview : FootballResult, holder: View){
        holder.findViewById<TextView>(R.id.team_name).apply {
            text = overview.homeTeam.name
        }
        holder.findViewById<TextView>(R.id.team_name2).apply {
            text = overview.awayTeam.name
        }
        holder.findViewById<TextView>(R.id.score1).apply {
            text = overview.homeGoal.toString()
        }
        holder.findViewById<TextView>(R.id.score2).apply {
            text = overview.awayGoal.toString()
        }
        holder.findViewById<TextView>(R.id.referee).apply {
            text = overview.referee
        }
    }

    private fun getMatchDetails(){
        val url = model.baseUrl + postfix + "?fixture=" + model.selectedMatchOverview.value!!.matchId
        val request = object : StringRequest(
            Method.GET, url, {
                adapter.data = processMatchDetails(it)
                adapter.notifyDataSetChanged()
            },{
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
        request.tag = "FB-FIXTURE-STATS"
        model.requestQueue.add(request)
    }

    private fun processMatchDetails(json : String) : ArrayList<StatLineData>{
        val ret = arrayListOf<StatLineData>()
        val obj = JSONObject(json)
        val arr = obj.getJSONArray("response")
        val homeStat = arr.getJSONObject(0)
        val awayStat = arr.getJSONObject(1)
        for (i in 0 until stats.size){
            val homeStatistics = homeStat.getJSONArray("statistics")
            val awayStatistics = awayStat.getJSONArray("statistics")

            val vLeft = if(homeStatistics.getJSONObject(i).isNull("value")){
                0
            }else{
                if(stats[i] == "Ball Possession"||stats[i] == "Passes %"){
                    val s = homeStatistics.getJSONObject(i).getString("value").substringBefore("%")
                    s.toInt()
                }else{
                    homeStatistics.getJSONObject(i).getInt("value")
                }

            }

            val vRight = if(awayStatistics.getJSONObject(i).isNull("value")){
                0
            }else{
                if(stats[i] == "Ball Possession"||stats[i] == "Passes %"){
                    val s = awayStatistics.getJSONObject(i).getString("value").substringBefore("%")
                    s.toInt()
                }else{
                    awayStatistics.getJSONObject(i).getInt("value")
                }

            }

            ret.add(StatLineData(stats[i], vLeft, vRight))
        }
        return ret
    }
}