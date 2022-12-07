package com.app.data.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.data.ListManager
import com.app.ngn.R
import java.text.SimpleDateFormat
import java.util.*

data class WeatherData(val temp: Double,
                       val type: WeatherType,
                       val description: String,
                       val time: Date,
                       val deg : Int,
                       val speed: Double,
                       val visibility : Long? = 0
)

class WeatherManager(data : List<WeatherData>) : ListManager<WeatherData>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.com_weather,
                parent,
                false
            )
        )
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        val metric = holder.itemView.resources.displayMetrics
        (holder as ViewHolder).apply {
            itemView.layoutParams.width = metric.widthPixels/3
            bind(data[position])
        }
    }

    private class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : WeatherData){
            val time = itemView.findViewById<TextView>(R.id.time)
            val status = itemView.findViewById<ImageView>(R.id.status)
            val temperature = itemView.findViewById<TextView>(R.id.temperature)
            val wind = itemView.findViewById<TextView>(R.id.wind)

            time.text = formatWeatherDate(data.time)
            status.setImageDrawable(
                ContextCompat.getDrawable(
                itemView.context,
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