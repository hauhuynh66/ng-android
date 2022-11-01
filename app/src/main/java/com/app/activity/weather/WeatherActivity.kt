package com.app.activity.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.app.activity.NavigatorActivity
import com.app.adapter.ListAdapter
import com.app.adapter.WeatherAdapter
import com.app.data.*
import com.app.data.WeatherType.Companion.get
import com.app.model.AppDatabase
import com.app.model.Location
import com.app.model.Setting
import com.app.ngn.R
import com.app.task.GetHttpTask
import com.app.task.TaskRunner
import com.app.util.Animation
import com.app.util.PermissionUtils.Companion.checkPermissions
import com.app.view.SunPositionView
import com.app.viewmodel.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess

/**
 * Weather Activity
 * Display current weather and 40 next forecast data
 */
class WeatherActivity : AppCompatActivity() {
    private val model : Weather by viewModels()
    private var lat : Double = 10.76
    private var lon : Double = 106.66
    private val default_lat = 10.76
    private val default_lon = 106.66
    private lateinit var contentView : ConstraintLayout
    private lateinit var progress : ProgressBar
    private lateinit var db: AppDatabase
    private lateinit var taskRunner: TaskRunner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_weather)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.apply{
            setDisplayHomeAsUpEnabled(true)
        }
        db = Room.databaseBuilder(this, AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()

        progress = findViewById(R.id.progress)
        contentView = findViewById(R.id.content_view)

        Animation.crossfade(arrayListOf(progress), arrayListOf(contentView))

        permissionsCheck()

        taskRunner = TaskRunner()

        getCity()

        val wParams = mapOf("lat" to lat, "lon" to lon, "appid" to model.key)
        taskRunner.execute(GetHttpTask(model.weatherUrl, params = wParams), object : TaskRunner.Callback<HttpResponse>{
            override fun onComplete(result: HttpResponse) {
                if(result.ok()){
                    model.weather.value = getWeatherData(result.get())
                    displayWeather(contentView, model.weather.value!!)

                    val fParams = mapOf("lat" to lat, "lon" to lon, "appid" to model.key, "cnt" to 40)
                    taskRunner.execute(GetHttpTask(model.forecastUrl, params = fParams), object : TaskRunner.Callback<HttpResponse>{
                        override fun onComplete(result: HttpResponse) {
                            if(result.ok()){
                                model.forecast.value = getForecastData(result.get())
                                displayForecast(contentView, model.forecast.value!!)
                                Animation.crossfade(arrayListOf(contentView), arrayListOf(progress), duration = 1000L)
                            }
                        }
                    })
                }
            }
        })
    }

    private fun getCity(){
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

    private fun getCity(name : String){
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

    private fun displayWeather(view : View, weatherData: WeatherData){
        findViewById<TextView>(R.id.temperature).apply {
            text = weatherData.temp.toString()
        }

        findViewById<TextView>(R.id.aqi).apply {
            text = weatherData.speed.toString()
        }

        findViewById<TextView>(R.id.status).apply {
            text = weatherData.description
        }

        val list = view.findViewById<RecyclerView>(R.id.list2)

        list.layoutManager = object : GridLayoutManager(this, 2){
            override fun generateLayoutParams(
                c: Context?,
                attrs: AttributeSet?
            ): RecyclerView.LayoutParams {
                return spanLayoutSize(super.generateLayoutParams(c, attrs))
            }

            private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
                var rows = (itemCount / spanCount)
                val left = itemCount % spanCount

                if ( left > 0){
                    rows++
                }

                layoutParams.width = (getHorizontalSpace() / spanCount.toDouble()).roundToInt()

                val height = (getVerticalSpace() / rows.toDouble()).roundToInt()
                layoutParams.height = height

                return layoutParams
            }

            private fun getHorizontalSpace(): Int {
                return width - paddingRight - paddingLeft
            }

            private fun getVerticalSpace(): Int {
                return height - paddingBottom - paddingTop
            }

            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        val lineOption = LineDisplayOption(gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL)

        val lineData = arrayListOf(
            LineData("Visibility", weatherData.visibility, option = lineOption),
            LineData("Filler", "Filler", option = lineOption),
            LineData("Filler", "Filler", option = lineOption),
            LineData("Filler", "Filler", option = lineOption),
            LineData("Filler", "Filler", option = lineOption),
            LineData("Filler", "Filler", option = lineOption),
            LineData("Filler", "Filler", option = lineOption),
            LineData("Filler", "Filler", option = lineOption)
        )

        list.adapter = ListAdapter(lineData, LineStyle.Style2)
    }

    private fun getWeatherData(json: String) : WeatherData?{
        return try {
            val obj = JSONObject(json)
            if(obj.getInt("cod") != 200){
                throw Exception(obj.getString("message"))
            }

            val description = obj.getJSONArray("weather").getJSONObject(0).getString("description")
            val main = obj.getJSONArray("weather").getJSONObject(0).getString("main")
            val temp = BigDecimal(obj.getJSONObject("main").getDouble("temp")-273.15)
                        .setScale(2, RoundingMode.FLOOR).toDouble()
            val deg = obj.getJSONObject("wind").getInt("deg")
            val speed = obj.getJSONObject("wind").getDouble("speed")
            val d = Date(obj.getLong("dt")*1000)
            val visibility = obj.getLong("visibility")

            WeatherData(
                temp,
                get(main),
                description,
                d,
                deg,
                speed,
                visibility
            )
        }catch (e : Exception){
            println(e.message)
            null
        }
    }

    private fun displayForecast(view : View, forecastData: ForecastData){
        supportActionBar.apply {
            findViewById<TextView>(R.id.title).text = forecastData.name
        }

        val forecastList = view.findViewById<RecyclerView>(R.id.list1)

        getCity(forecastData.name)

        forecastList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        forecastList.adapter = WeatherAdapter(forecastData.data)
    }

    private fun getForecastData(json: String) : ForecastData?{
        val list = arrayListOf<WeatherData>()
        return try {
            val obj = JSONObject(json)
            val cityName = obj.getJSONObject("city").getString("name")
            val fcList = obj.getJSONArray("list")

            val sunPositionView = findViewById<SunPositionView>(R.id.gauge)
            sunPositionView.display(obj.getJSONObject("city")
                .getLong("sunrise"), obj.getJSONObject("city").getLong("sunset"))

            for(i in 0 until 40){
                val f = fcList.getJSONObject(i)
                val mainObj = f.getJSONObject("main")
                val temp = BigDecimal(mainObj.getDouble("temp")-273.15)
                            .setScale(2, RoundingMode.FLOOR).toDouble()
                val main = f.getJSONArray("weather").getJSONObject(0).getString("main")
                val des = f.getJSONArray("weather").getJSONObject(0).getString("description")
                val d = Date(f.getLong("dt")*1000)
                val wind = f.getJSONObject("wind")

                val data = WeatherData(
                    temp,
                    get(main),
                    des,
                    d,
                    wind.getInt("deg"),
                    wind.getDouble("speed"),
                )
                list.add(data)
            }
            ForecastData(list, cityName)
        }catch (e : Exception){
            println(e.message)
            null
        }


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
            android.R.id.home->{
                onBackPressed()
            }
            else->{

            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, NavigatorActivity::class.java)
        intent.putExtra("from", "weather")
        startActivity(intent)
    }
}