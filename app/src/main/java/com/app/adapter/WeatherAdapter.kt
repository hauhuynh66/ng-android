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
import com.app.util.Format.Companion.formatWeatherDate
import com.app.util.Generator.Companion.getWeatherIcon

class WeatherAdapter(private val context:Context, var data:ArrayList<WeatherData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return WeatherViewHolder(inflater.inflate(R.layout.com_weather, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val metric = context.resources.displayMetrics
        (holder as WeatherViewHolder).apply {
            v.layoutParams.width = metric.widthPixels/3
            bind(data[position], context)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class WeatherViewHolder(var v:View) : RecyclerView.ViewHolder(v){
        fun bind(data : WeatherData, context: Context){
            val des = v.findViewById<TextView>(R.id.sub_des)
            val temp = v.findViewById<TextView>(R.id.sub_temp)
            val humid = v.findViewById<TextView>(R.id.sub_humid)
            val icon = v.findViewById<ImageView>(R.id.weather_icon)
            val time = v.findViewById<TextView>(R.id.com_weather_dx)
            time.text = formatWeatherDate(data.time)
            des.text = data.description
            temp.text = data.temp.toString()
            humid.text = data.humid.toString()
            icon.setImageDrawable(getWeatherIcon(data.type, context))
            v.setOnClickListener {
                println(time.text)
            }
        }
    }
}