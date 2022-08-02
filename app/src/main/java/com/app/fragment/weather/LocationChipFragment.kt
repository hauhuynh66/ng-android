package com.app.fragment.weather

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.activity.GoogleMapActivity
import com.app.model.AppDatabase
import com.app.model.Location
import com.app.ngn.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray

class LocationChipFragment(val data: ArrayList<String>, val listener : Listener) : Fragment() {
    private val key = "1f21f91e5b111cf398a465df830c423b"
    private var url = "http://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={key}"

    private lateinit var db: AppDatabase
    private lateinit var requestQueue : RequestQueue
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_chips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chips = view.findViewById<ChipGroup>(R.id.chips)
        requestQueue = Volley.newRequestQueue(requireContext())
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()
        val currentChip = Chip(requireContext())
        currentChip.text = "Current Location"
        currentChip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_location_on)
        currentChip.setOnClickListener {
            val map = Intent(requireContext(), GoogleMapActivity::class.java)
            startActivity(map)
        }
        chips.addView(currentChip)
        url = url.replace("{key}", key)

        for(d in data){
            val chip = Chip(requireContext())
            chip.text = d
            val currentUrl = url.replace("{city}", d)
            chip.setOnClickListener {
                requestQueue.add(
                    StringRequest(currentUrl, {
                        processCity(it)
                    },{
                        println(it.message)
                    })
                )
            }
            chips.addView(chip)
        }
    }

    private fun processCity(json : String){
        val obj = JSONArray(json).getJSONObject(0)
        val name = obj.getString("name")
        val lat = obj.getDouble("lat")
        val lon = obj.getDouble("lon")
        var isSuccess = 0
        runBlocking {
            withContext(Dispatchers.IO){
                isSuccess = db.locationRepository().checkInsert(Location(name, lon, lat))
            }
        }

        if(isSuccess==1){
            listener.onAdded()
        }
    }

    interface Listener{
        fun onAdded()
    }
}