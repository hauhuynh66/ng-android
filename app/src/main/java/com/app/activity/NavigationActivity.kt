package com.app.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.fragment.MiscFragment
import com.app.fragment.NoteFragment
import com.app.fragment.WeatherFragment
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner
import com.app.util.Check.Companion.checkPermissions
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
        permissionCheck()
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

    private fun permissionCheck(){
        val permissions = arrayListOf<String>()
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if(!checkPermissions(this, permissions)){
            val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permission -> run {
                    when{
                        permission.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false)->{

                        }
                        permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)->{

                        }
                        permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)->{

                        }
                        permission.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)->{

                        }
                        else->{
                            exitProcess(0)
                        }
                    }
                }
            }
            requestPermissions.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        }
    }
}