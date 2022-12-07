package com.app.activity

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import com.app.ngn.R
import com.app.util.Permission
import com.app.util.ViewUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.system.exitProcess

class GoogleMapActivity:AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val location : MutableLiveData<Location> = MutableLiveData()
    private lateinit var mapFragment : SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_map)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        ViewUtils.configTitle(toolbar, true)
        toolbar.findViewById<TextView>(R.id.title).text = getString(R.string.map_title)
        setSupportActionBar(toolbar)
        val requestPermission = Permission(
            listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            this,
            object : Permission.Callback{
                override fun onGranted() {
                    val fusedLocationProviderClient = FusedLocationProviderClient(this@GoogleMapActivity)
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener{
                        location.value = it
                    }
                }

                override fun onDenied(permission: String) {
                    super.onDenied(permission)
                    exitProcess(0)
                }
            }
        )
        requestPermission.checkOrRequest()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        location.observe(this){
            if(location.value == null){
                return@observe
            }

            val lngLat = LatLng(location.value!!.longitude, location.value!!.latitude)
            mMap.addMarker(
                MarkerOptions()
                    .position(lngLat)
                    .title("Current Location"))

            mMap.setMinZoomPreference(13f)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lngLat))
        }
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