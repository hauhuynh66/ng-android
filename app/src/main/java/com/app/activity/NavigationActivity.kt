package com.app.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.fragment.*
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var auth:FirebaseAuth
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_navigation)
        auth = Firebase.auth
        val user = auth.currentUser ?: exitProcess(0)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val displayName = navigationView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_name)
        val displayEmail = navigationView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_email)
        val displayImg = navigationView.getHeaderView(0).findViewById<ImageView>(R.id.nav_header_img)
        displayName.text = user.displayName ?: "User"
        displayEmail.text = user.email
        if(user.photoUrl!=null) {
            val taskRunner = TaskRunner()
            taskRunner.execute(ImageCallable(user.photoUrl.toString()), object: TaskRunner.Callback<Bitmap?>{
                override fun onComplete(result: Bitmap?) {
                    displayImg.setImageBitmap(result)
                }
            })
        }
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
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        toolbar.menu.clear()
        if(supportActionBar!=null){
            supportActionBar!!.apply {
                when(item.itemId){
                    R.id.nav_note->{
                        this.title = "Note"
                        supportFragmentManager.beginTransaction().replace(R.id.container,  NoteFragment(), "NOTE").commit()
                    }
                    R.id.nav_test->{
                        this.title = "Test"
                        supportFragmentManager.beginTransaction().replace(R.id.container,  TestFragment(), "TEST").commit()
                    }
                    R.id.nav_menu_item->{
                        this.title = "Item"
                        supportFragmentManager.beginTransaction().replace(R.id.container,  ItemFragment(), "ITEM").commit()
                    }
                    R.id.nav_menu_misc->{
                        this.title = "Others"
                        supportFragmentManager.beginTransaction().replace(R.id.container,  MiscFragment(), "MISC").commit()
                    }
                    R.id.nav_menu_setting->{
                        val intent = Intent(this@NavigationActivity, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                    else->{
                        val f = supportFragmentManager.findFragmentById(R.id.container)
                        if(f !is WeatherFragment){
                            supportActionBar!!.title = "Weather"
                            supportFragmentManager.beginTransaction().replace(R.id.container,  WeatherFragment(), "WEATHER").commit()
                        }
                    }
                }
            }
        }

        this.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

    }
}