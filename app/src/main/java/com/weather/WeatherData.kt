package com.weather

import com.app.ngn.R
import java.util.*

data class WeatherData(val temp: Double,
                       val type: WeatherType,
                       val description: String,
                       val time: Date,
                       val deg : Int,
                       val speed: Double,
                       val visibility : Long? = 0
){
    companion object{
        fun getIcon(des: WeatherType): Int {
            return when(des){
                WeatherType.Rain ->{
                    R.drawable.ic_light_rain
                }
                WeatherType.Drizzle ->{
                    R.drawable.ic_heavy_rain
                }
                WeatherType.Clouds ->{
                    R.drawable.ic_cloudy
                }
                else -> {
                    R.drawable.ic_sunny
                }
            }
        }
    }
}

enum class TemperatureUnit{
    Celsius,
    Kelvin,
    Fahrenheit;

    companion object{
        fun convert(value : Number, from : TemperatureUnit, to : TemperatureUnit) : Number{
            return when(from){
                Celsius ->{
                    fromCelsius(value, to)
                }
                Fahrenheit ->{
                    fromFahrenheit(value, to)
                }
                Kelvin ->{
                    fromKelvin(value, to)
                }
            }
        }

        private fun fromCelsius(value : Number, to : TemperatureUnit) : Number{
            return when(to){
                Kelvin ->{
                    value.toDouble() + 273.15
                }
                Fahrenheit ->{
                    (value.toDouble() * 9.0/5.0) + 32
                }
                else->{
                    value
                }
            }
        }

        private fun fromFahrenheit(value : Number, to : TemperatureUnit) : Number{
            return when(to){
                Celsius ->{
                    (value.toDouble() - 32) * 5.0/9.0
                }
                Fahrenheit ->{
                    ((value.toDouble() - 32) * 5.0/9.0) + 273.15
                }
                else->{
                    value
                }
            }
        }

        private fun fromKelvin(value : Number, to : TemperatureUnit) : Number{
            return when(to){
                Celsius ->{
                    value.toDouble() - 273.15
                }
                Fahrenheit ->{
                    ((value.toDouble() - 273.15) * 9.0/5.0) + 32
                }
                else->{
                    value
                }
            }
        }
    }
}

enum class SunState{
    State1,
    State2,
    State3
}

data class ForecastData constructor(var data:ArrayList<WeatherData>, var name:String)

enum class WeatherType{
    Thunderstorm,
    Drizzle,
    Rain,
    Snow,
    Atmosphere,
    Clear,
    Clouds;
    companion object{
        fun get(des : String) : WeatherType {
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
    }
}