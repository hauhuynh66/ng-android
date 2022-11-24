package com.app.data.weather

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