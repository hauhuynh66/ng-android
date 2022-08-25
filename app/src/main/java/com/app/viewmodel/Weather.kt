package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.data.ForecastData
import com.app.data.WeatherData

class Weather : ViewModel(){
    enum class WeatherState{
        WeatherDetail,
        CityList,
        CitySearch
    }


    val key = "1f21f91e5b111cf398a465df830c423b"
    val url = "https://api.openweathermap.org/data/2.5/{mode}?lat={lat}&lon={lon}&appid={key}"
    val currentState = MutableLiveData(WeatherState.WeatherDetail)
    val currentWeather = MutableLiveData<WeatherData>()
    val forecast = MutableLiveData<ForecastData>()
}