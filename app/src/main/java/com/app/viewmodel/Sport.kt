package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activity.sport.SportMainActivity
import com.app.data.FootballTeam

class Sport : ViewModel() {
    val state = MutableLiveData(SportMainActivity.SportStates.SportList)
    val baseUrl : String = "https://v3.football.api-sports.io"
    val apiKey : String = "207de13e407253dfbe98859d90d378ce"
    val apiHost : String = "v3.football.api-sports.io"
    val selectedClub : MutableLiveData<FootballTeam?> = MutableLiveData(null)
}