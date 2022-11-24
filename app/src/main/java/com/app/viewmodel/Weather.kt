package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.data.weather.ForecastData
import com.app.data.weather.WeatherData

class Weather : ViewModel(){
    enum class WeatherState{
        WeatherDetail,
        CityList,
        CitySearch
    }

    val key = "1f21f91e5b111cf398a465df830c423b"
    val weatherUrl = "https://api.openweathermap.org/data/2.5/weather"
    val forecastUrl = "https://api.openweathermap.org/data/2.5/forecast"

    val state = MutableLiveData(WeatherState.WeatherDetail)

    val weather = MutableLiveData<WeatherData>()
    val forecast = MutableLiveData<ForecastData>()

}