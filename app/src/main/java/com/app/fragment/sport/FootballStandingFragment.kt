package com.app.fragment.sport

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.app.adapter.FootballStandingAdapter
import com.app.data.FootballStandingData
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.util.FootballAPI
import com.app.viewmodel.Football
import org.json.JSONObject
import java.util.HashMap

class FootballStandingFragment : Fragment() {
    private val model : Football by activityViewModels()
    private val postfix = "/standings"
    private lateinit var progress : ProgressBar
    private lateinit var adapter : FootballStandingAdapter
    private lateinit var list : RecyclerView
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
        progress = view.findViewById(R.id.progress)
        list = view.findViewById(R.id.item_list)
        adapter = FootballStandingAdapter(requireContext(), arrayListOf())
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)

        getStandings("39", "2022")
    }

    private fun getStandings(strLeagueId : String, strSeason : String){
        crossfade(arrayListOf(progress), arrayListOf(list), 1000)
        val url = model.baseUrl + postfix + "?league=" + strLeagueId + "&season=" + strSeason
        val request = object : StringRequest(Request.Method.GET, url,
            {
                println(it)
                adapter.data = processStandingData(it)
                adapter.notifyDataSetChanged()
                crossfade(arrayListOf(list), arrayListOf(progress), 1000)
            }, {
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
        request.tag = "FB-STANDINGS"
        model.requestQueue.add(request)
    }

    private fun processStandingData(json : String) : ArrayList<FootballStandingData?>{
        val ret = arrayListOf<FootballStandingData?>()
        ret.add(null)
        val obj = JSONObject(json)
        val res = obj.getJSONArray("response")
        if(res.length()>0){
            val standingsList = res.getJSONObject(0).getJSONObject("league").getJSONArray("standings").getJSONArray(0)
            if(standingsList.length()>0){
                for(i in 0 until standingsList.length()){
                    val data = standingsList.getJSONObject(i)
                    val form = data.getString("form")
                    val team = FootballAPI.getTeam(data.getJSONObject("team"))
                    val score = FootballAPI.getScore(data.getJSONObject("all").getJSONObject("goals"))
                    val match = FootballAPI.getMatch(data.getJSONObject("all"))

                    ret.add(FootballStandingData(team, match, score, data.getInt("points"), form))
                }
            }
        }
        return ret
    }
}