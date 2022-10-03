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
import com.app.activity.rd.RDMainActivity
import com.app.fragment.MainFragment
import com.app.fragment.MiscFragment
import com.app.fragment.note.NoteListFragment
import com.app.ngn.R
import com.app.viewmodel.Authentication
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlin.system.exitProcess

class NavigatorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val model : Authentication by viewModels()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
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
        toolbar = findViewById(R.id.toolbar)
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
                    supportFragmentManager.beginTransaction().replace(R.id.container,  MiscFragment(), "MISC").commit()
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
        toolbar.menu.clear()
        if(supportActionBar!=null){
            val transaction = supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.flip_right_in,
                    R.anim.flip_right_out,
                    R.anim.flip_left_in,
                    R.anim.flip_left_out
            )

            supportActionBar!!.apply {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
                when(item.itemId){
                    R.id.nav_note->{
                        this.title = "Note"
                        if(currentFragment !is NoteListFragment){
                            transaction.replace(R.id.container,  NoteListFragment(), "NOTE").commit()
                            transaction.addToBackStack("NOTE")
                        }
                    }
                    R.id.nav_menu_misc->{
                        this.title = "Others"
                        if(currentFragment !is MiscFragment) {
                            transaction.replace(R.id.container, MiscFragment(), "MISC").commit()
                            transaction.addToBackStack("MISC")
                        }
                    }
                    R.id.nav_rd->{
                        this.title = "RD"
                        val intent = Intent(this@NavigatorActivity, RDMainActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.nav_menu_setting->{
                        val intent = Intent(this@NavigatorActivity, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                    else->{
                        if(currentFragment !is MainFragment){
                            this.title = "Home"
                            transaction.replace(R.id.container,  MainFragment(), "MAIN").commit()
                            transaction.addToBackStack("MAIN")
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
        if(supportFragmentManager.backStackEntryCount==0){
            when(count){
                0->{
                    prev = System.currentTimeMillis()
                    count++
                    Toast.makeText(this, "Press back one more time to exit", Toast.LENGTH_SHORT)
                }
                1->{
                    curr = System.currentTimeMillis()
                    val diff = curr - prev
                    if( diff < 1000L){
                        exitProcess(0)
                    }else{
                        prev = curr
                    }
                }
            }
        }else{
            supportFragmentManager.popBackStack()
        }
    }
}