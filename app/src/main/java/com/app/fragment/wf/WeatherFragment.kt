package com.app.fragment.wf

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.activity.GoogleMapActivity
import com.app.data.ForecastData
import com.app.dialog.GraphDialog
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.apache.commons.text.WordUtils
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import com.app.adapter.WeatherAdapter as WeatherAdapter

class WeatherFragment : Fragment(){
    private lateinit var requestQueue:RequestQueue
    private lateinit var progressBar: ProgressBar
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var contentLayout : View
    private lateinit var location: Location
    private lateinit var forecastData: ForecastData
    private val cnt = 40
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fg_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contentLayout = view.findViewById(R.id.contentLayout)
        contentLayout.visibility = View.INVISIBLE
        this.progressBar = view.findViewById(R.id.progress)
        this.progressBar.visibility = View.VISIBLE
        this.requestQueue = Volley.newRequestQueue(this.requireContext())
        forecastData = ForecastData(arrayListOf(),"")
        val graph = view.findViewById<Button>(R.id.graph)
        val map = view.findViewById<Button>(R.id.map)
        graph.setOnClickListener{
            GraphDialog(forecastData).show(requireActivity().supportFragmentManager, "Graph")
        }

        map.setOnClickListener {
            val intent = Intent(requireActivity(), GoogleMapActivity::class.java)
            intent.putExtra("lon", location.longitude)
            intent.putExtra("lat", location.latitude)
            startActivity(intent)
        }
        val key = "1f21f91e5b111cf398a465df830c423b"
        var url = "https://api.openweathermap.org/data/2.5/{forecast}?lat={lat}&lon={lon}&appid={key}&cnt={cnt}"
        url = url.replace("{key}", key)
        url = url.replace("{cnt}", cnt.toString())

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            run{
                if(it!=null){
                    this.location = it
                    url = url.replace("{lat}", it.latitude.toString())
                    url = url.replace("{lon}", it.longitude.toString())
                    processRequest(url, contentLayout)
                }
            }
        }.addOnFailureListener{
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun processWeatherAPI(responseJSON: String):ForecastData{
        val json = JSONObject(responseJSON)
        val list = ArrayList<com.app.data.WeatherData>()
        val weatherList = json.getJSONArray("list")
        for (i in 0 until cnt){
            val data = weatherList.getJSONObject(i)
            val weather = data.getJSONObject("main")
            val weatherArr = data.getJSONArray("weather")
            val type = weatherArr.getJSONObject(0).getString("main")
            val des = WordUtils.capitalize(weatherArr.getJSONObject(0).getString("description"))
            val d = Date(data.getLong("dt") * 1000)
            val df = DecimalFormat("##,##")
            val temp = df.format(weather.getDouble("temp") - 273.15).toDouble()
            list.add(
                com.app.data.WeatherData(temp, weather.getLong("humidity"),des, d, type)
            )
        }
        val name = json.getJSONObject("city").getString("name")

        return ForecastData(list, name)
    }

    override fun onDestroy() {
        requestQueue.cancelAll("FORECAST");
        super.onDestroy()
    }

    private fun processRequest(url:String, view: View){
        val forecastRequest = StringRequest(Request.Method.GET, url,
            {
                    response-> run {
                    forecastData = processWeatherAPI(response)
                    val listView:RecyclerView = view.findViewById(R.id.weather_list)
                    listView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    listView.adapter = WeatherAdapter(requireActivity(), forecastData.data)
                    crossfade(arrayListOf(contentLayout), arrayListOf(progressBar), duration = 800)
                }
            }, { error-> run {
                    if(error is TimeoutError){
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this.context, "Request timeout", Toast.LENGTH_LONG).show()
                    }
                }
            }).setTag("FORECAST")
        forecastRequest.retryPolicy =
            DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        requestQueue.add(forecastRequest)
    }
}