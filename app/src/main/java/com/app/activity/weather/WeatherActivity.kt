package com.app.activity.weather

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.activity.NavigatorActivity
import com.app.adapter.DetailAdapter
import com.app.adapter.WeatherAdapter
import com.app.data.DetailData
import com.app.data.ForecastData
import com.app.data.WeatherData
import com.app.helper.SpanGridLayoutManager
import com.app.model.AppDatabase
import com.app.model.Location
import com.app.model.Setting
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.util.Check.Companion.checkPermissions
import com.app.view.SunPositionView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.system.exitProcess

class WeatherActivity : AppCompatActivity() {
    private val key = "1f21f91e5b111cf398a465df830c423b"
    private var url = "https://api.openweathermap.org/data/2.5/{mode}?lat={lat}&lon={lon}&appid={key}"
    private var lat : Double = 10.76
    private var lon : Double = 106.66
    private val default_lat = 10.76
    private val default_lon = 106.66
    private lateinit var progressBar : ProgressBar
    private lateinit var contentView : ConstraintLayout
    private lateinit var requestQueue: RequestQueue
    private var forecastData: LiveData<ForecastData> = MutableLiveData(ForecastData(arrayListOf(), ""))
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_weather)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        permissionsCheck()
        requestQueue = Volley.newRequestQueue(this)
        progressBar = findViewById(R.id.progress)
        contentView = findViewById(R.id.content_view)
        contentView.visibility = View.GONE
        db = Room.databaseBuilder(this, AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()
        process()
        getWeather()
        getForecast()
    }

    private fun process(){
        runBlocking {
            withContext(Dispatchers.IO){
                val setting = db.settingRepository().getProperty("current_city")
                val location : Location? = if(setting ==null){
                    db.locationRepository().getFirst()
                }else{
                    db.locationRepository().getByName(setting)
                }

                if(location==null){
                    lat = default_lat
                    lon = default_lon
                }else{
                    lat = location.lat
                    lon = location.lon
                }
            }
        }
    }

    private fun permissionsCheck(){
        val requiredPermissions = arrayListOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permission->
                    run {
                        when{
                            permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)->{

                            }
                            permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)->{

                            }
                            else->{
                                exitProcess(0)
                            }
                        }
                    }
        }
        if(!checkPermissions(this,requiredPermissions)){
            launcher.launch(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            )
        }
    }

    private fun getWeather(){
        var weatherUrl = url.replace("{mode}", "weather")
        weatherUrl = weatherUrl.replace("{lat}", lat.toString())
        weatherUrl = weatherUrl.replace("{lon}", lon.toString())
        weatherUrl = weatherUrl.replace("{key}", key)

        println(weatherUrl)

        val weatherRequest = StringRequest(Request.Method.GET, weatherUrl,
            {
                processWeather(it)
            },
            {
                println(it)
            }
        )
        requestQueue.add(weatherRequest)
    }

    private fun dbProcess(name : String){
        runBlocking {
            withContext(Dispatchers.IO){
                val location = db.locationRepository().getByName(name)
                if(location==null){
                    val currentName = db.locationRepository().getByCoordinate(lat, lon)
                    if(currentName==null){
                        db.locationRepository().insert(Location(name, lon, lat))
                    }
                    val setting = db.settingRepository().getProperty("current_city")
                    if(setting==null){
                        db.settingRepository().insert(Setting("current_city", name))
                    }else{
                        db.settingRepository().update("current_city", name)
                    }
                }
            }
        }
    }

    private fun processWeather(json: String){
        val temperature = findViewById<TextView>(R.id.temperature)
        val extra = findViewById<TextView>(R.id.aqi)
        val status = findViewById<TextView>(R.id.status)
        val obj = JSONObject(json)
        val temp = BigDecimal.valueOf((obj.getJSONObject("main").getDouble("temp") - 272.15))
        val list2 = findViewById<RecyclerView>(R.id.list2)
        val detailData = arrayListOf<DetailData>()
        list2.layoutManager = SpanGridLayoutManager(this, 2)

        temperature.text = temp.setScale(2, RoundingMode.FLOOR).toDouble().toString()
        extra.text = obj.getJSONObject("wind").getDouble("speed").toString()
        status.text = obj.getJSONArray("weather").getJSONObject(0).getString("description")

        detailData.add(DetailData("Humidity", obj.getJSONObject("main").getLong("humidity")))
        detailData.add(DetailData("Feels Like",
            BigDecimal.valueOf((obj.getJSONObject("main").getDouble("feels_like") - 272.15))
                .setScale(2,RoundingMode.FLOOR).toDouble().toString())
        )
        detailData.add(DetailData("Visibility", obj.getLong("visibility")))
        detailData.add(DetailData("Filler", "Filler"))
        detailData.add(DetailData("Filler", "Filler"))
        detailData.add(DetailData("Filler", "Filler"))
        list2.adapter = DetailAdapter(this, detailData, 2)
    }

    private fun getForecast(){
        var forecastUrl = url.replace("{mode}", "forecast")
        forecastUrl = forecastUrl.replace("{lat}", lat.toString())
        forecastUrl = forecastUrl.replace("{lon}", lon.toString())
        forecastUrl = forecastUrl.replace("{key}", key)
        forecastUrl = forecastUrl.plus("&cnt=40")
        println(forecastUrl)

        val forecastRequest = StringRequest(Request.Method.GET, forecastUrl,
            {
                processForecast(it)
            },
            {
                println(it)
            }
        )
        requestQueue.add(forecastRequest)
    }

    private fun processForecast(json: String){
        val forecastList = findViewById<RecyclerView>(R.id.list1)
        val obj = JSONObject(json)
        val fcList = obj.getJSONArray("list")
        for(i in 0 until 40){
            val f = fcList.getJSONObject(i)
            val main = f.getJSONObject("main")
            val temp = BigDecimal(main.getDouble("temp")-272.15).setScale(2, RoundingMode.FLOOR).toDouble()
            val des = f.getJSONArray("weather").getJSONObject(0).getString("main")
            val d = Date(f.getLong("dt")*1000)
            val wind = f.getJSONObject("wind")
            val data = WeatherData(
                temp,
                des,
                d,
                wind.getInt("deg"),
                wind.getDouble("speed")
            )
            forecastData.value!!.data.add(data)
        }
        forecastData.value!!.name = obj.getJSONObject("city").getString("name")

        val title = findViewById<TextView>(R.id.toolbar_title)
        title.text = forecastData.value!!.name

        dbProcess(forecastData.value!!.name)

        forecastList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        forecastList.adapter = WeatherAdapter(this, forecastData.value!!.data)

        val sunPositionView = findViewById<SunPositionView>(R.id.gauge)
        sunPositionView.sunrise = obj.getJSONObject("city").getLong("sunrise")
        sunPositionView.sunset = obj.getJSONObject("city").getLong("sunset")
        sunPositionView.reload(System.currentTimeMillis()/1000)

        crossfade(arrayListOf(contentView), arrayListOf(progressBar), 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add->{
                val list = Intent(this, LocationListActivity::class.java)
                startActivity(list)
            }
            else->{

            }
        }
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this, NavigatorActivity::class.java)
        intent.putExtra("from", "weather")
        startActivity(intent)
    }
}