package com.app.data.weather

import com.app.ngn.R

enum class WeatherType{
    Thunderstorm,
    Drizzle,
    Rain,
    Snow,
    Atmosphere,
    Clear,
    Clouds;
    companion object{
        fun fromString(des : String) : WeatherType {
            return when(des){
                "Thunderstorm"->{
                    Thunderstorm
                }
                "Drizzle"->{
                    Drizzle
                }
                "Rain"->{
                    Rain
                }
                "Snow"->{
                    Snow
                }
                "Atmosphere"->{
                    Atmosphere
                }
                "Clouds"->{
                    Clouds
                }
                else -> {
                    Clear
                }
            }
        }

        fun getDrawable(des: WeatherType): Int {
            return when(des){
                Rain ->{
                    R.drawable.ic_light_rain
                }
                Drizzle ->{
                    R.drawable.ic_heavy_rain
                }
                Clouds ->{
                    R.drawable.ic_cloudy
                }
                else -> {
                    R.drawable.ic_sunny
                }
            }
        }
    }
}