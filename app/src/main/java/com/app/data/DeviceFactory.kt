package com.app.data

class DeviceFactory {
    enum class DeviceType{
        LIGHT, FAN, SPRINKLER, THERMOMETER
    }
    fun getDevice(type : DeviceType) : DeviceImpl {
        return when(type){
            DeviceType.LIGHT->{
                Light()
            }
            DeviceType.FAN->{
                Fan()
            }
            DeviceType.SPRINKLER->{
                Sprinkler()
            }
            DeviceType.THERMOMETER->{
                Thermometer()
            }
        }
    }
}