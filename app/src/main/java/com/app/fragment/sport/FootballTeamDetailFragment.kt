package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.data.FootballTeamDetail
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.viewmodel.Sport

class FootballTeamDetailFragment : Fragment() {
    private val model : Sport by activityViewModels()
    private val postfix : String = "/teams"
    private var teamDetail : FootballTeamDetail? = null
    private val isDisplay : MutableLiveData<Boolean> = MutableLiveData(true)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_football_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = view.findViewById<ConstraintLayout>(R.id.content)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        this.isDisplay.observe(requireActivity()){
            when(isDisplay.value){
                true->{
                    crossfade(arrayListOf(content), arrayListOf(progress), 1000)
                }
                false->{
                    crossfade(arrayListOf(progress), arrayListOf(content), 1000)
                }
            }
        }

        getTeamDetail(model.selectedClub.value!!.id)
    }

    private fun getTeamDetail(teamId : Int){
        val url = model.baseUrl + postfix + "?id=" + teamId
        val teamRequest = object : StringRequest(
            Method.GET, url,
            {
                println(it)
                this.isDisplay.value = true
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
        teamRequest.tag = "FB-TEAM"
        model.requestQueue.add(teamRequest)
    }

    fun processTeamDetailGeneral(json : String){

    }
}