package com.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.fragment.AboutFragment
import com.app.fragment.ActionFragment
import com.app.fragment.MainFragment
import com.app.fragment.TestFragment
import com.app.fragment.NoteListFragment
import com.app.ngn.R
import com.app.viewmodel.Authentication
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlin.system.exitProcess

class NavigatorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val model : Authentication by viewModels()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var displayName : TextView
    private lateinit var displayEmail : TextView
    private lateinit var displayImage : ImageView
    private var count: Int = 0
    private var prev: Long = 0L
    private var curr: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_navigation)
        val user = model.firebaseAuth.currentUser ?: exitProcess(0)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        displayName = navigationView.getHeaderView(0).findViewById(R.id.nav_header_name)
        displayEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email)
        displayImage = navigationView.getHeaderView(0).findViewById(R.id.nav_header_img)

        navigationView.getHeaderView(0).findViewById<ImageView>(R.id.logout).apply {
            setOnClickListener {
                model.firebaseAuth.signOut()
                val intent = Intent(this@NavigatorActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        displayName.text = user.displayName ?: "User"
        displayEmail.text = user.email
        if(user.photoUrl!=null){
            Picasso.get().load(user.photoUrl).into(displayImage)
        }

        navigationView.setNavigationItemSelectedListener(this)
        this.drawerLayout = findViewById(R.id.drawer_layout)

        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        if(intent.extras!=null){
            when(intent.extras!!.getString("from")){
                "weather"->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,  ActionFragment(), "MISC").commit()
                    supportActionBar!!.title = "Others"
                }
            }
        }else{
            if(savedInstanceState==null){
                supportFragmentManager.beginTransaction().replace(R.id.container,  MainFragment(), "MAIN").commit()
                supportActionBar!!.title = "Home"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        model.firebaseAuth.currentUser!!.apply {
            this@NavigatorActivity.displayName.text = this.displayName
            if(this.photoUrl!=null){
                Picasso.get().load(this.photoUrl).into(this@NavigatorActivity.displayImage)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        var currentTitle = "Home"
        supportActionBar!!.apply {
            when(item.itemId){
                R.id.nav_note->{
                    currentTitle = "Note"
                    transaction.replace(R.id.container,  NoteListFragment()).commit()
                }
                R.id.nav_menu_misc->{
                    currentTitle = "Miscellaneous"
                    transaction.replace(R.id.container,  ActionFragment()).commit()
                }
                R.id.nav_rd->{
                    val intent = Intent(this@NavigatorActivity, NewsMainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_menu_setting->{
                    val intent = Intent(this@NavigatorActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_menu_about->{
                    currentTitle = "About"
                    transaction.replace(R.id.container,  AboutFragment(), "ABOUT").commit()
                }
                R.id.nav_menu_test->{
                    currentTitle = "Test"
                    transaction.replace(R.id.container,  TestFragment(), "TEST").commit()
                }
                else->{
                    transaction.replace(R.id.container,  MainFragment(), "MAIN").commit()
                }
            }

            this.title = currentTitle
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
        when(count){
            0->{
                prev = System.currentTimeMillis()
                count = 1
                Toast.makeText(this, "Press back one more time to exit", Toast.LENGTH_SHORT).show()
            }
            1->{
                curr = System.currentTimeMillis()
                val diff = curr - prev
                if( diff < Toast.LENGTH_SHORT){
                    exitProcess(0)
                }else{
                    prev = curr
                    count = 0
                }
            }
        }
    }
}