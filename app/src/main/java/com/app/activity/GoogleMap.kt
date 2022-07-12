package com.app.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.ngn.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMap:AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var data:MutableMap<String, Double>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_map)
        val bundle = intent.extras
        data = hashMapOf()
        bundle!!.apply {
            data["lat"] = this.getDouble("lat")
            data["lon"] = this.getDouble("lon")
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Map"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val defaultLocation = LatLng(data["lat"]!!, data["lon"]!!)
        mMap.addMarker(
            MarkerOptions()
            .position(defaultLocation)
            .title("Current Location"))
        mMap.setMinZoomPreference(13f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
        }
        return true
    }
}