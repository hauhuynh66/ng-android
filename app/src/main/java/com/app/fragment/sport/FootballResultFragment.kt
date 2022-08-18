package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.activity.sport.SportMainActivity
import com.app.adapter.FootballResultAdapter
import com.app.adapter.NumberArrayAdapter
import com.app.data.FootballResult
import com.app.data.FootballTeam
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.viewmodel.Sport
import org.json.JSONObject

class FootballResultFragment : Fragment() {
    private lateinit var requestQueue: RequestQueue
    private val model : Sport by activityViewModels()
    private val postfix : String = "/fixtures"
    private val isDisplay = MutableLiveData(true)
    private val round = MutableLiveData(1)
    private var result = arrayListOf<FootballResult>()

    private fun getResultByRound(round : Int, strLeagueId : String){
        this.isDisplay.value = false
        val url = model.baseUrl + postfix + "?league=" + strLeagueId + "&round=Regular Season - " + round + "&season=" + 2022
        val fbRequest = object : StringRequest(
            Method.GET, url,
            {
                println(it)
                result = processFootballResult(it)
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
        fbRequest.tag = "FB-LIST"
        requestQueue.add(fbRequest)
    }

    private fun processFootballResult(json : String) : ArrayList<FootballResult>{
        val resArray = JSONObject(json).getJSONArray("response")
        val arr = arrayListOf<FootballResult>()
        for(i in 0 until resArray.length()){
            val obj = resArray.getJSONObject(i)
            val referee = obj.getJSONObject("fixture").getString("referee")
            val teams = obj.getJSONObject("teams")
            val homeTeam = FootballTeam(teams.getJSONObject("home").getInt("id"),
                teams.getJSONObject("home").getString("name"),
                teams.getJSONObject("home").getString("logo"))
            val awayTeam = FootballTeam(teams.getJSONObject("away").getInt("id"),
                teams.getJSONObject("away").getString("name"),
                teams.getJSONObject("away").getString("logo"))

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

            arr.add(FootballResult(homeTeam, awayTeam, referee, homeScore, awayScore))
        }

        return arr
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_football_result_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestQueue = Volley.newRequestQueue(requireContext())
        val leagueSpinner = view.findViewById<Spinner>(R.id.league)
        val leagueSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.fbl, android.R.layout.simple_spinner_item)
        leagueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leagueSpinner.adapter = leagueSpinnerAdapter

        val roundSpinner = view.findViewById<Spinner>(R.id.round)
        roundSpinner.adapter = NumberArrayAdapter(requireActivity(),R.layout.com_text, R.id.text ,
            arrayListOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38))
        roundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p1!!.apply {
                    val value = findViewById<TextView>(R.id.text).text.toString().toInt()
                    if(value!=this@FootballResultFragment.round.value){
                        this@FootballResultFragment.round.value = value
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val list = view.findViewById<RecyclerView>(R.id.item_list)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        this.round.observe(requireActivity()){
            getResultByRound(this.round.value!!, "39")
        }
        this.isDisplay.observe(requireActivity()){
            when(it){
                true->{
                    crossfade(arrayListOf(list), arrayListOf(progress), 1000)
                    println(result.size)
                    list.adapter = FootballResultAdapter(requireContext(), result, object : FootballResultAdapter.Callback{
                        override fun onTeamClick(team: FootballTeam) {
                            model.selectedClub.value = team
                            model.state.value = SportMainActivity.SportStates.FootballTeamDetail
                        }
                    })
                }
                false->{
                    crossfade(arrayListOf(progress), arrayListOf(list), 1000)
                }
            }
        }
    }
}