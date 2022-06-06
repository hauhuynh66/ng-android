package com.app.adapter

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.WeatherData
import com.app.ngn.R

class WeatherAdapter(private val context:Activity, var data:ArrayList<WeatherData>):
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return WeatherViewHolder(inflater.inflate(R.layout.com_weather, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val metric = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(metric)
        holder.v.layoutParams.width = metric.widthPixels/3
        val des = holder.v.findViewById<TextView>(R.id.sub_des)
        val temp = holder.v.findViewById<TextView>(R.id.sub_temp)
        val humid = holder.v.findViewById<TextView>(R.id.sub_humid)
        des.text = data[position].description
        temp.text = data[position].temp.toString()
        humid.text = data[position].humid.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class WeatherViewHolder(var v:View):RecyclerView.ViewHolder(v){

    }
}