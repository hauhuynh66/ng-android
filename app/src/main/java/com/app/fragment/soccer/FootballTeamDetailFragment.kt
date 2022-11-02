package com.app.fragment.soccer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.app.data.FootballTeamDetail
import com.app.data.HttpResponse
import com.app.ngn.R
import com.app.task.GetHttpTask
import com.app.task.TaskRunner
import com.app.util.Animation.Companion.crossfade
import com.app.viewmodel.FootballDisplay
import com.squareup.picasso.Picasso
import org.json.JSONObject

class FootballTeamDetailFragment : Fragment() {
    private val model : FootballDisplay by activityViewModels()
    private val postfix : String = "/teams"
    private val isDisplay : MutableLiveData<Boolean> = MutableLiveData(true)
    private var teamDetail : FootballTeamDetail? = null
    private lateinit var taskRunner: TaskRunner
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
        taskRunner = TaskRunner()
        val content = view.findViewById<ConstraintLayout>(R.id.content)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        this.isDisplay.observe(requireActivity()){
            /*when(isDisplay.value){
                true->{
                    crossfade(arrayListOf(content), arrayListOf(progress), 1000)
                }
                false->{
                    crossfade(arrayListOf(progress), arrayListOf(content), 1000)
                }
            }*/
        }
        getTeamDetail(model.team!!.id)
    }

    private fun getTeamDetail(teamId : Int){
        val url = getString(R.string.football_api_url) + postfix + "?id=" + teamId

        val headers = mutableMapOf(
            "x-rapidapi-host" to getString(R.string.football_api_host),
            "x-rapidapi-key" to getString(R.string.football_api_key)
        )

        val task = GetHttpTask(url, headers)
        taskRunner.execute(task, object : TaskRunner.Callback<HttpResponse>{
            override fun onComplete(result: HttpResponse) {
                if(result.ok()){
                    /*suspend {
                        teamDetail = processTeamDetailGeneral(result.getString())
                    }

                    if(teamDetail!=null){
                        display(teamDetail!!, view)
                    }
                    this@FootballTeamDetailFragment.isDisplay.value = true*/
                }
            }
        })
    }

    fun processTeamDetailGeneral(json : String) : FootballTeamDetail?{
        val obj = JSONObject(json)
        return if(obj.getJSONArray("errors").length() > 0){
            null
        }else{
            val res = obj.getJSONArray("response").getJSONObject(0)
            val team = res.getJSONObject("team")
            //val venue = res.getJSONObject("venue")
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
            view.findViewById<TextView>(R.id.team_name).apply {
                text = team.name
            }

            view.findViewById<TextView>(R.id.team_country).apply {
                text = team.country
            }

            view.findViewById<TextView>(R.id.team_founded).apply {
                text = team.founded.toString()
            }
            Picasso.get().load(team.iconUrl).into(view.findViewById<ImageView>(R.id.team_icon))
        }
    }
}