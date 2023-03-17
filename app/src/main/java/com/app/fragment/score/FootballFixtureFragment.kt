package com.app.fragment.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.score.MainActivity
import com.app.adapter.FootballFixtureAdapter
import com.app.data.HttpResponse
import com.app.data.score.FootballResult
import com.app.data.score.FootballTeam
import com.app.data.score.ScoreContextHolder
import com.app.data.score.ScoreFlag
import com.app.ngn.R
import com.app.task.GetHttpTask
import com.app.task.TaskRunner
import com.general.Animation.Companion.crossfade
import com.general.DateTimeUtils.Companion.formatDate
import com.general.JsonUtils
import org.json.JSONObject
import java.util.*

class FootballFixtureFragment : Fragment() {
    private val data : ScoreContextHolder by activityViewModels()
    private val postfix : String = "/fixtures"
    private var result : ArrayList<FootballResult> = arrayListOf()
    private lateinit var progress : ProgressBar
    private lateinit var list : RecyclerView
    private lateinit var adapter : FootballFixtureAdapter
    private lateinit var parent : MainActivity
    private var firstAttached : Int = 0

    private fun getResult(strLeagueId : String, strDate : String){
        crossfade(progress, list)
        val url = getString(R.string.football_api_url) + postfix + "?league=" + strLeagueId + "&season=" + 2022 + "&date=" + strDate

        val headers = mutableMapOf(
            "x-rapidapi-host" to getString(R.string.football_api_host),
            "x-rapidapi-key" to getString(R.string.football_api_key)
        )

        val task = GetHttpTask(url, header = headers)
        val taskRunner = TaskRunner()
        taskRunner.execute(task, object : TaskRunner.Callback<HttpResponse>{
            override fun onComplete(result: HttpResponse) {
                if(result.ok()){
                    this@FootballFixtureFragment.result = processFootballResult(result.get())
                    adapter.data = this@FootballFixtureFragment.result
                    adapter.notifyDataSetChanged()
                    crossfade(list, progress)
                }
            }
        })
    }

    private fun processFootballResult(json : String) : ArrayList<FootballResult>{
        val resArray = JSONObject(json).getJSONArray("response")
        val error = JSONObject(json).getJSONArray("errors")
        if(error.length()>0){
            return arrayListOf()
        }else{
            val arr = arrayListOf<FootballResult>()
            for(i in 0 until resArray.length()){
                val obj = resArray.getJSONObject(i)
                val referee = obj.getJSONObject("fixture").getString("referee").substringBefore(",")
                val matchId = obj.getJSONObject("fixture").getLong("id")
                val teams = obj.getJSONObject("teams")
                val homeTeam = JsonUtils.getTeam(teams.getJSONObject("home"))
                val awayTeam = JsonUtils.getTeam(teams.getJSONObject("away"))
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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        firstAttached++
    }

    override fun onResume() {
        super.onResume()
        if(firstAttached < 2)
        {
            getResult("39", formatDate(Date(1677974400000	), "yyyy-MM-dd"))
        }
        firstAttached++
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
        this.parent = this.activity as MainActivity

        /*val flBtn = view.findViewById<FloatingActionButton>(R.id.option_button)
        flBtn.apply {
            visibility = View.VISIBLE
            setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_calendar_today))
            setOnClickListener {
                val dpListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    calendar.set(Calendar.YEAR, y)
                    calendar.set(Calendar.MONTH, m)
                    calendar.set(Calendar.DATE, d)
                    //model.currentDate.value = calendar.time
                }

                DatePickerDialog(requireContext(),
                    dpListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
                ).show()
            }
        }*/

        list = view.findViewById(R.id.item_list)
        progress = view.findViewById(R.id.progress)

        adapter = FootballFixtureAdapter(result, object : FootballFixtureAdapter.Callback{
            override fun onTeamClick(team: FootballTeam) {
                this@FootballFixtureFragment.data.selectedTeamId = team.id
                this@FootballFixtureFragment.parent.navigate(ScoreFlag.Team)
            }

            override fun onClick(overview: FootballResult) {
                this@FootballFixtureFragment.data.currentMatchOverview = overview
                this@FootballFixtureFragment.parent.navigate(ScoreFlag.Match)
            }
        })
        /*model.currentDate.observe(requireActivity()){
            calendar.time = model.currentDate.value!!
            getResult(model.currentLeague.value.toString(), formatDate(calendar.time, "yyyy-MM-dd"))
        }*/
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list.adapter = adapter
    }
}