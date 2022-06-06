package com.app.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.fragment.WeatherFragment
import com.app.ngn.R
import com.google.android.material.navigation.NavigationView

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_navigation)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        this.drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container,  WeatherFragment()).commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            else->{
                supportFragmentManager.beginTransaction().replace(R.id.container,  WeatherFragment()).commit()
            }
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
                (android.R.id.home)-> {
                    this.drawerLayout.openDrawer(GravityCompat.START)
                }
        }
        return super.onOptionsItemSelected(item)
    }
}