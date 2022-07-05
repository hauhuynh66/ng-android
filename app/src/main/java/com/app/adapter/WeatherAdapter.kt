package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.WeatherData
import com.app.ngn.R
import com.app.util.Generator.Companion.getWeatherIcon

class WeatherAdapter(private val context:Context, var data:ArrayList<WeatherData>):
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return WeatherViewHolder(inflater.inflate(R.layout.com_weather, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val metric = context.resources.displayMetrics
        holder.v.layoutParams.width = metric.widthPixels/3
        val des = holder.v.findViewById<TextView>(R.id.sub_des)
        val temp = holder.v.findViewById<TextView>(R.id.sub_temp)
        val humid = holder.v.findViewById<TextView>(R.id.sub_humid)
        val icon = holder.v.findViewById<ImageView>(R.id.weather_icon)
        des.text = this.data[position].description
        temp.text = this.data[position].temp.toString()
        humid.text = this.data[position].humid.toString()
        icon.setImageDrawable(getWeatherIcon(this.data[position].description, this.context))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class WeatherViewHolder(var v:View):RecyclerView.ViewHolder(v){

    }
}