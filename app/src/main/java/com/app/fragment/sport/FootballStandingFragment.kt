package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.app.adapter.FootballStandingAdapter
import com.app.data.FootballStandingData
import com.app.ngn.R
import com.app.util.FootballAPI
import com.app.viewmodel.Football
import org.json.JSONObject

class FootballStandingFragment : Fragment() {
    private val model : Football by viewModels()
    private val postfix = "/standings"
    private lateinit var progress : ProgressBar
    private lateinit var adapter : FootballStandingAdapter
    private lateinit var standingsData : ArrayList<FootballStandingData>
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
        progress = view.findViewById(R.id.progress)
        standingsData = arrayListOf()
        val list = view.findViewById<RecyclerView>(R.id.item_list)
        adapter = FootballStandingAdapter(requireContext(), standingsData)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun getStandings(strLeagueId : String, strSeason : String){
        val url = model.baseUrl + postfix + "?league=" + strLeagueId + "&season=" + strSeason
        val request = StringRequest(Request.Method.GET, url,
            {
                processStandingData(it)
            }, {
                println(it.message)
            }
        )
        request.tag = "FB-STANDINGS"
        model.requestQueue.add(request)
    }

    private fun processStandingData(json : String) : ArrayList<FootballStandingData>{
        val ret = arrayListOf<FootballStandingData>()
        val obj = JSONObject(json)
        val res = obj.getJSONArray("response")
        if(res.length()>0){
            val standingsList = res.getJSONObject(0).getJSONArray("standings").getJSONArray(0)
            if(standingsList.length()>0){
                for(i in 0 until standingsList.length()){
                    val data = standingsList.getJSONObject(i)
                    val team = FootballAPI.getTeam(data.getJSONObject("team"))
                }
            }
        }
        return ret
    }
}