package com.app.activity

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.ngn.R
import com.app.util.PermissionUtils.Companion.checkPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapActivity:AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var location : Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_map)
        selfCheck()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Map"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location = it
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lngLat = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
            .position(lngLat)
            .title("Current Location"))
        mMap.setMinZoomPreference(13f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lngLat))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
        }
        return true
    }

    private fun selfCheck(){
        val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions -> run {
                when{
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)->{

                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)->{

                    }else->{
                        super.onBackPressed()
                    }
                }
            }
        }
        val permissions = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if(!checkPermissions(this, permissions)){
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }
}