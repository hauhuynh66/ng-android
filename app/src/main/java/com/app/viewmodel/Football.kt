package com.app.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.app.data.FootballTeam

class Football : ViewModel() {
    enum class State{
        Fixtures,
        MatchDetails,
        TeamDetails,
        PlayerDetails,
        Table
    }
    val state = MutableLiveData(State.Fixtures)
    val baseUrl : String = "https://v3.football.api-sports.io"
    val apiKey : String = "207de13e407253dfbe98859d90d378ce"
    val apiHost : String = "v3.football.api-sports.io"
    val selectedClub : MutableLiveData<FootballTeam?> = MutableLiveData(null)
    lateinit var requestQueue : RequestQueue

    fun init(context: Context){
        requestQueue = Volley.newRequestQueue(context)
    }
}