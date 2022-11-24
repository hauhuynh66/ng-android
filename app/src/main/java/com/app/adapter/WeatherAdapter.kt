package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.data.weather.WeatherData
import com.app.data.weather.WeatherType
import com.app.ngn.R
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(var data:ArrayList<WeatherData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        return WeatherViewHolder(inflater.inflate(R.layout.com_weather, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val metric = context.resources.displayMetrics
        (holder as WeatherViewHolder).apply {
            v.layoutParams.width = metric.widthPixels/3
            bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class WeatherViewHolder(var v:View) : RecyclerView.ViewHolder(v){
        fun bind(data : WeatherData){
            val time = v.findViewById<TextView>(R.id.time)
            val status = v.findViewById<ImageView>(R.id.status)
            val temperature = v.findViewById<TextView>(R.id.temperature)
            val wind = v.findViewById<TextView>(R.id.wind)

            time.text = formatWeatherDate(data.time)
            status.setImageDrawable(ContextCompat.getDrawable(v.context,
                WeatherType.getDrawable(data.type)
            ))
            temperature.text = data.temp.toString()
            wind.text = "${data.speed} km/h"
        }

        private fun formatWeatherDate(date: Date) : String{
            val sf = SimpleDateFormat("dd HH:mm", Locale.getDefault())
            return try {
                sf.format(date)
            }catch (e:Exception){
                ""
            }
        }
    }

}