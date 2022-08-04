package com.app.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    private var forecastData : MutableLiveData<ForecastData> = MutableLiveData(ForecastData(arrayListOf(), ""))
}