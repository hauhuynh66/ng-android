package com.app.data

import java.util.*

data class WeatherData(val temp: Double,
                       val type: WeatherType,
                       val description: String,
                       val time: Date,
                       val deg : Int,
                       val speed: Double,
                       val visibility : Long? = 0
)

enum class TemperatureUnit{
    Celsius,
    Kelvin,
    Fahrenheit;

    companion object{
        fun convert(value : Number ,from : TemperatureUnit, to : TemperatureUnit) : Number{
            return when(from){
                Celsius->{
                    fromCelsius(value, to)
                }
                Fahrenheit->{
                    fromFahrenheit(value, to)
                }
                Kelvin->{
                    fromKelvin(value, to)
                }
            }
        }

        private fun fromCelsius(value : Number, to : TemperatureUnit) : Number{
            return when(to){
                Kelvin->{
                    value.toDouble() + 273.15
                }
                Fahrenheit->{
                    (value.toDouble() * 9.0/5.0) + 32
                }
                else->{
                    value
                }
            }
        }

        private fun fromFahrenheit(value : Number, to : TemperatureUnit) : Number{
            return when(to){
                Celsius->{
                    (value.toDouble() - 32) * 5.0/9.0
                }
                Fahrenheit->{
                    ((value.toDouble() - 32) * 5.0/9.0) + 273.15
                }
                else->{
                    value
                }
            }
        }

        private fun fromKelvin(value : Number, to : TemperatureUnit) : Number{
            return when(to){
                Celsius->{
                    value.toDouble() - 273.15
                }
                Fahrenheit->{
                    ((value.toDouble() - 273.15) * 9.0/5.0) + 32
                }
                else->{
                    value
                }
            }
        }
    }
}

enum class WeatherType{
    Thunderstorm,
    Drizzle,
    Rain,
    Snow,
    Atmosphere,
    Clear,
    Clouds;
    companion object{
        fun get(des : String) : WeatherType{
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