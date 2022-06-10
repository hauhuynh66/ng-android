package com.app.activity

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.fragment.ItemFragment
import com.app.fragment.NoteFragment
import com.app.fragment.WeatherFragment
import com.app.ngn.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var auth:FirebaseAuth
    private lateinit var currentLocation:WeatherFragment.SimpleLocation
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_navigation)
        auth = Firebase.auth
        val user = auth.currentUser ?: exitProcess(0)
        //val displayName = findViewById<TextView>(R.id.displayName)
        //val displayEmail = findViewById<TextView>(R.id.email)
        //val userImg = findViewById<ImageView>(R.id.userImg)
        //displayName.text = user.displayName!!
        //displayEmail.text = user.email
        //userImg.setImageBitmap(user.photoUrl)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        this.drawerLayout = findViewById(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container,  WeatherFragment(), "WEATHER").commit()
            supportActionBar!!.title = "Weather"
        }

        val permission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permission ->
                run {
                    when {
                        permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                        }
                        else -> {

                        }
                    }
                }
        }

        permission.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_note->{
                supportActionBar!!.title = "Note"
                supportFragmentManager.beginTransaction().replace(R.id.container,  NoteFragment(), "NOTE").commit()
            }
            R.id.nav_test->{

            }
            R.id.nav_menu_item->{
                supportActionBar!!.title = "Item"
                supportFragmentManager.beginTransaction().replace(R.id.container,  ItemFragment(), "ITEM").commit()
            }
            else->{
                val f = supportFragmentManager.findFragmentById(R.id.container)
                if(f !is WeatherFragment){
                    supportActionBar!!.title = "Weather"
                    supportFragmentManager.beginTransaction().replace(R.id.container,  WeatherFragment(), "WEATHER").commit()
                }

            }
        }
        this.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
                android.R.id.home->{
                    this.drawerLayout.openDrawer(GravityCompat.START)
                }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

    }
}