package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.adapter.WeatherAdapter
import com.app.data.ForecastData
import com.app.data.WeatherData
import com.app.ngn.R
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherFragment: Fragment() {
    private lateinit var requestQueue:RequestQueue
    private var forecast:ForecastData? = null;
    private val cnt = 7;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fg_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.requestQueue = Volley.newRequestQueue(this.activity?.applicationContext)
        val key = "1f21f91e5b111cf398a465df830c423b"
        var url = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={key}&cnt={cnt}"
        url = url.replace("{key}", key)
        url = url.replace("{lon}", "106.6667")
        url = url.replace("{lat}", "10.75")
        url = url.replace("{cnt}", cnt.toString())
        val forecastRequest = StringRequest(Request.Method.GET, url,
            {
                response->
                run {
                    forecast = processWeatherAPI(response)
                    updateUI(forecast!!, view)
                    val layoutManager = LinearLayoutManager(this.requireContext())
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    val listView:RecyclerView = view.findViewById(R.id.weather_list)
                    listView.layoutManager = layoutManager
                    listView.adapter = WeatherAdapter(this.requireActivity(), forecast!!.data)
                }
            }, { error->
                run {
                    println(error)
                }
        }).setTag("FORECAST")
        requestQueue.add(forecastRequest)
    }

    private fun updateUI(forecastData:ForecastData, v:View){
        val cityName = v.findViewById<TextView>(R.id.city_name)
        val description = v.findViewById<TextView>(R.id.weather_des)
        val temp = v.findViewById<TextView>(R.id.main_temp)
        val humid = v.findViewById<TextView>(R.id.main_humid)
        val data = forecastData.data[0]
        cityName.text = forecastData.name
        description.text = data.description
        temp.text = data.temp.toString()
        humid.text = data.humid.toString()
        for(i in 1 until cnt){
            val weatherData = forecastData.data[i];

        }
    }

    private fun processWeatherAPI(responseJSON: String):ForecastData{
        val json = JSONObject(responseJSON)
        val list = ArrayList<WeatherData>()
        val weatherList = json.getJSONArray("list")
        for (i in 0 until cnt){
            val data = weatherList.getJSONObject(i);
            val weather = data.getJSONObject("main")
            val weatherArr = data.getJSONArray("weather")
            val des = weatherArr.getJSONObject(0).getString("description")
            val d = Date(data.getLong("dt"))
            val df = DecimalFormat("##.##")
            val temp = df.format(weather.getDouble("temp") - 273.15).toDouble()
            list.add(
                WeatherData(temp, weather.getLong("humidity"),des, d))

        }
        val name = json.getJSONObject("city").getString("name")

        return ForecastData(list, name)

    }
}