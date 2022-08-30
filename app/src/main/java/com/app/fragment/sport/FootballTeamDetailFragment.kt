package com.app.fragment.sport

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.android.volley.toolbox.StringRequest
import com.app.data.FootballTeamDetail
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner
import com.app.util.Animation.Companion.crossfade
import com.app.viewmodel.Football
import org.json.JSONObject

class FootballTeamDetailFragment : Fragment() {
    private val model : Football by activityViewModels()
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
        view.findViewById<Button>(R.id.standing).apply {
            setOnClickListener {
                model.state.value = Football.State.Table
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
                teamDetail = processTeamDetailGeneral(it)
                if(teamDetail!=null){
                    display(teamDetail!!, view)
                }
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

    fun processTeamDetailGeneral(json : String) : FootballTeamDetail?{
        val obj = JSONObject(json)
        return if(obj.getJSONArray("response").length()<1){
            null
        }else{
            val res = obj.getJSONArray("response").getJSONObject(0)
            val team = res.getJSONObject("team")
            val venue = res.getJSONObject("venue")
            FootballTeamDetail(
                team.getString("name"),
                team.getString("logo"),
                team.getString("country"),
                team.getInt("founded")
            )
        }
    }

    fun display(team : FootballTeamDetail, view : View?){
        if(view!=null){
            val runner = TaskRunner()
            val icon = view.findViewById<ImageView>(R.id.team_icon)
            runner.execute(ImageCallable(team.iconUrl), object : TaskRunner.Callback<Bitmap?>{
                override fun onComplete(result: Bitmap?) {
                    icon.setImageBitmap(result)
                }
            })
            view.findViewById<TextView>(R.id.team_name).apply {
                text = team.name
            }

            view.findViewById<TextView>(R.id.team_country).apply {
                text = team.country
            }

            view.findViewById<TextView>(R.id.team_founded).apply {
                text = team.founded.toString()
            }
        }
    }
}