package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.data.Actuator
import com.app.data.Device
import com.app.data.Light
import com.app.data.Sensor
import com.app.ngn.R

class DeviceAdapter(val context : Context, val data : ArrayList<Device>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            0->{
                LightViewHolder(inflater.inflate(R.layout.com_device_1, parent, false))
            }
            1->{
                SensorViewHolder(inflater.inflate(R.layout.com_device_1, parent, false))
            }
            else->{
                ActuatorViewHolder(inflater.inflate(R.layout.com_device_2, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position]::class.java){
            Light::class.java->{
                0
            }
            Sensor::class.java->{
                1
            }
            Actuator::class.java->{
                2
            }
            else->{
                3
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0->{

            }
            1->{

            }
            else->{

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class LightViewHolder(val v : View) : RecyclerView.ViewHolder(v){

    }

    class SensorViewHolder(val v : View) : RecyclerView.ViewHolder(v){

    }

    class ActuatorViewHolder(val v : View) : RecyclerView.ViewHolder(v){

    }
}