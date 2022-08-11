package com.app.data

class DeviceFactory {
    enum class DeviceType{
        LIGHT, SENSOR, ACTUATOR
    }
    fun getDevice(type : DeviceType) : Device{
        return when(type){
            DeviceType.LIGHT->{
                Light()
            }
            DeviceType.SENSOR->{
                Sensor()
            }
            DeviceType.ACTUATOR->{
                Actuator()
            }
        }
    }
}