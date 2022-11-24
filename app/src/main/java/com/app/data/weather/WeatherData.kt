package com.app.data.weather

import java.util.*

data class WeatherData(val temp: Double,
                       val type: WeatherType,
                       val description: String,
                       val time: Date,
                       val deg : Int,
                       val speed: Double,
                       val visibility : Long? = 0
)