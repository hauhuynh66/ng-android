package com.app.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.app.data.FootballResult
import com.app.data.FootballStandingData
import com.app.data.FootballTeam
import java.util.*
import kotlin.collections.ArrayList

class Football : ViewModel() {
    val baseUrl : String = "https://v3.football.api-sports.io"
    val apiKey : String = "207de13e407253dfbe98859d90d378ce"
    val apiHost : String = "v3.football.api-sports.io"
    val currentDate = MutableLiveData(Date())
    val league = MutableLiveData(39)
    val selectedClub : MutableLiveData<FootballTeam?> = MutableLiveData(null)
    val selectedMatchOverview = MutableLiveData<FootballResult>(null)
    val currentStandingData = MutableLiveData<ArrayList<FootballStandingData>>(arrayListOf())

    lateinit var requestQueue : RequestQueue

    fun init(context: Context){
        requestQueue = Volley.newRequestQueue(context)
    }
}