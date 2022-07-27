package com.app.activity.weather

import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.app.fragment.weather.ChipGroupFragment
import com.app.fragment.weather.LocationListFragment
import com.app.model.AppDatabase
import com.app.ngn.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LocationListActivity : AppCompatActivity() {
    private val locations : ArrayList<String> = arrayListOf()
    private var addLocations : ArrayList<String> = arrayListOf()
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.apply {
            title = "Locations"
            setDisplayHomeAsUpEnabled(true)
        }
        db = Room.databaseBuilder(this, AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()
        runBlocking {
            withContext(Dispatchers.IO){
                addLocations = db.locationRepository().getAll().map { it.name }.toCollection(ArrayList())
            }
        }
        //
        locations.add("London")
        locations.add("Paris")
        locations.add("Berlin")
        locations.add("Prague")
        locations.add("New York")
        locations.add("Hanoi")
        locations.add("Tokyo")
        locations.add("Moscow")

        val search = findViewById<SearchView>(R.id.search_view)
        supportFragmentManager
            .beginTransaction().replace(R.id.container, LocationListFragment()).commit()

        search.setOnSearchClickListener {
            supportFragmentManager
                .beginTransaction().replace(R.id.container, ChipGroupFragment(locations)).commit()
        }

        search.setOnCloseListener {
            search.onActionViewCollapsed()
            supportFragmentManager
                .beginTransaction().replace(R.id.container, LocationListFragment()).commit()
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
            else->{

            }
        }
        return true
    }
}