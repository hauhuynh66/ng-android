package com.app.activity.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.fragment.weather.LocationListFragment
import com.app.ngn.R

class LocationListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.apply {
            title = "Locations"
            setDisplayHomeAsUpEnabled(true)
        }
        supportFragmentManager
            .beginTransaction().replace(R.id.container, LocationListFragment()).commit()
    }
}