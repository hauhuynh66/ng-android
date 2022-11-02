package com.app.fragment.soccer

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.FootballStandingAdapter
import com.app.data.FootballStanding
import com.app.data.HttpResponse
import com.app.ngn.R
import com.app.task.GetHttpTask
import com.app.task.TaskRunner
import com.app.util.Animation.Companion.crossfade
import com.app.util.JsonUtils
import com.app.viewmodel.Football
import org.json.JSONObject

class FootballStandingFragment : Fragment() {
    private val model : Football by activityViewModels()
    private val postfix = "/standings"
    private lateinit var progress : ProgressBar
    private lateinit var adapter : FootballStandingAdapter
    private lateinit var list : RecyclerView
    private lateinit var taskRunner : TaskRunner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            return inflater.inflate(R.layout.fg_list, container, false)
        }
        return inflater.inflate(R.layout.fg_hs_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskRunner = TaskRunner()

        progress = view.findViewById(R.id.progress)
        list = view.findViewById(R.id.item_list)
        adapter = FootballStandingAdapter(arrayListOf())
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)

        getStandings(model.currentLeague.value.toString(), "2022")
    }

    private fun getStandings(strLeagueId : String, strSeason : String){
        crossfade(arrayListOf(progress), arrayListOf(list), 1000)
        val url = getString(R.string.football_api_url) + postfix + "?league=" + strLeagueId + "&season=" + strSeason

        val headers = mutableMapOf(
            "x-rapidapi-host" to getString(R.string.football_api_host),
            "x-rapidapi-key" to getString(R.string.football_api_key)
        )
        val task = GetHttpTask(url, headers)
        taskRunner.execute(task, object : TaskRunner.Callback<HttpResponse>{
            override fun onComplete(result: HttpResponse) {
                if(result.ok()){
                    /*suspend {
                        adapter.data = processStandingData(result.getString())
                    }
                    adapter.notifyDataSetChanged()
                    crossfade(arrayListOf(list), arrayListOf(progress))*/
                }
            }
        })
    }

    private fun processStandingData(json : String) : ArrayList<FootballStanding?>{
        val ret = arrayListOf<FootballStanding?>()
        //header
        ret.add(null)
        //header
        val obj = JSONObject(json)
        val res = obj.getJSONArray("response")
        if(res.length()>0){
            val standingsList = res.getJSONObject(0).getJSONObject("league").getJSONArray("standings").getJSONArray(0)
            if(standingsList.length()>0){
                for(i in 0 until standingsList.length()){
                    val data = standingsList.getJSONObject(i)
                    val form = data.getString("form")
                    val team = JsonUtils.getTeam(data.getJSONObject("team"))
                    val score = JsonUtils.getScore(data.getJSONObject("all").getJSONObject("goals"))
                    val match = JsonUtils.getMatch(data.getJSONObject("all"))

                    ret.add(FootballStanding(team, match, score, data.getInt("points"), form))
                }
            }
        }
        return ret
    }
}