package com.app.fragment.score

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
import com.app.data.score.FootballTeamDetail
import com.app.data.HttpResponse
import com.app.data.score.ScoreContextHolder
import com.app.ngn.R
import com.app.task.GetHttpTask
import com.app.task.TaskRunner
import com.general.Animation.Companion.crossfade
import com.squareup.picasso.Picasso
import org.json.JSONObject

class FootballTeamDetailFragment : Fragment() {
    private val data : ScoreContextHolder by activityViewModels()
    private val postfix : String = "/teams"
    private var teamDetail : FootballTeamDetail? = null
    private lateinit var content : ConstraintLayout
    private lateinit var progress : ProgressBar
    private var attachedTimes: Int = 0
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
        content = view.findViewById(R.id.content)
        progress = view.findViewById(R.id.progress)
    }

    override fun onResume() {
        super.onResume()
        println(attachedTimes)
        if(attachedTimes < 1)
        {
            if(data.selectedTeamId!=null)
            {
                getTeamDetail(data.selectedTeamId!!)
            }
        }
        attachedTimes ++
    }

    private fun getTeamDetail(teamId : Int){
        crossfade(progress, content)
        val url = getString(R.string.football_api_url) + postfix + "?id=" + teamId

        val headers = mutableMapOf(
            "x-rapidapi-host" to getString(R.string.football_api_host),
            "x-rapidapi-key" to getString(R.string.football_api_key)
        )

        val task = GetHttpTask(url,header = headers)
        val taskRunner = TaskRunner()
        taskRunner.execute(task, object : TaskRunner.Callback<HttpResponse>{
            override fun onComplete(result: HttpResponse) {
                if(result.ok()){
                    this@FootballTeamDetailFragment.teamDetail = processTeamDetailGeneral(result.get())

                    display(this@FootballTeamDetailFragment.teamDetail!!, content)
                    crossfade(content, progress)
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