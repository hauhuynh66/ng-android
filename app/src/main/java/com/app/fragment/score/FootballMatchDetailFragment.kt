package com.app.fragment.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.CustomListAdapter
import com.app.data.HttpResponse
import com.app.data.StatLine
import com.app.data.StatManager
import com.app.ngn.R
import com.app.task.GetHttpTask
import com.app.task.TaskRunner
import com.app.data.score.FootballResult
import com.squareup.picasso.Picasso
import org.json.JSONObject

class FootballMatchDetailFragment : Fragment() {
    private val postfix = "/fixtures/statistics"
    private lateinit var data : List<StatLine>
    private lateinit var taskRunner : TaskRunner
    private lateinit var list : RecyclerView
    private lateinit var progress : ProgressBar
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
        //redisplayOverview(model.matchOverview!!, view.findViewById(R.id.overview_group))
        taskRunner = TaskRunner()
        data = arrayListOf()
        val statManager = StatManager(data)

        list = view.findViewById(R.id.item_list)
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list.adapter = CustomListAdapter(statManager)

        progress = view.findViewById(R.id.progress)

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
            text = if(overview.homeGoal != null) overview.homeGoal.toString() else "-"
        }

        holder.findViewById<TextView>(R.id.score2).apply {
            text = if(overview.awayGoal != null) overview.awayGoal.toString() else "-"
        }

        holder.findViewById<TextView>(R.id.referee).apply {
            text = overview.referee
        }

        Picasso.get().load(overview.homeTeam.iconUrl).into(holder.findViewById<ImageView>(R.id.team_icon))
        Picasso.get().load(overview.awayTeam.iconUrl).into(holder.findViewById<ImageView>(R.id.team_icon2))
    }

    private fun getMatchDetails(){
        //val url = getString(R.string.football_api_url) + postfix + "?fixture=" + model.matchOverview!!.matchId
        val url = getString(R.string.football_api_url) + postfix + "?fixture="

        val headers = mutableMapOf(
            "x-rapidapi-host" to getString(R.string.football_api_host),
            "x-rapidapi-key" to getString(R.string.football_api_key)
        )
        val task = GetHttpTask(url, headers)
        taskRunner.execute(task, object : TaskRunner.Callback<HttpResponse>{
            override fun onComplete(result: HttpResponse) {
                if(result.ok()){
                    /*suspend {
                        adapter.data = processMatchDetails(result.getString())
                    }
                    adapter.notifyDataSetChanged()
                    Animation.crossfade(arrayListOf(list), arrayListOf(progress))*/
                }
            }
        })
    }

    private fun processMatchDetails(json : String) : ArrayList<StatLine>{
        val ret = arrayListOf<StatLine>()
        val obj = JSONObject(json)
        val arr = obj.getJSONArray("response")
        if(arr.length()<1){
            return ret
        }
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
                    if(homeStatistics.getJSONObject(i).isNull("value")){
                        0
                    }else{
                        homeStatistics.getJSONObject(i).getInt("value")
                    }
                }

            }

            val vRight = if(awayStatistics.getJSONObject(i).isNull("value")){
                0
            }else{
                if(stats[i] == "Ball Possession"||stats[i] == "Passes %"){
                    val s = awayStatistics.getJSONObject(i).getString("value").substringBefore("%")
                    s.toInt()
                }else{
                    if(awayStatistics.getJSONObject(i).isNull("value")){
                        0
                    }else{
                        awayStatistics.getJSONObject(i).getInt("value")
                    }
                }

            }

            ret.add(StatLine(stats[i], vLeft, vRight))
        }
        return ret
    }
}